package com.latihangoding.tittle_tattle.ui.music

import android.app.Application
import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.latihangoding.tittle_tattle.App
import com.latihangoding.tittle_tattle.utils.SharedPreferenceHelper
import com.latihangoding.tittle_tattle.vo.AudioModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MusicViewModel @Inject constructor(app: Application) : AndroidViewModel(app) {

    private val _audios = MutableLiveData<List<AudioModel>>()
    val audios: LiveData<List<AudioModel>>
        get() = _audios

    private val _isList = MutableLiveData<Boolean>()
    val isList: LiveData<Boolean>
        get() = _isList

    init {
        checkIsList()
        viewModelScope.launch {
            getMediaAudio()
        }
    }

    fun checkIsList(isSet: Boolean = false) {
        val helper = SharedPreferenceHelper(getApplication<App>().applicationContext, "music")
        Timber.d("berfore $helper.isList")
        if (isSet)
            helper.isList = !helper.isList

        Timber.d("after $helper.isList")
        _isList.postValue(helper.isList)
    }

    private suspend fun getMediaAudio() {
        withContext(Dispatchers.IO) {
            val myAudio = mutableListOf<AudioModel>()
            val columns =
                arrayOf(
                    MediaStore.Audio.Media._ID,
                    MediaStore.Audio.Media.TITLE,
                    MediaStore.Audio.Media.ALBUM_ID,
                    MediaStore.Audio.Media.MIME_TYPE,
                    MediaStore.Audio.Media.DISPLAY_NAME
                )
            val orderBy = MediaStore.Audio.Media.DATE_MODIFIED
            getApplication<App>().contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                columns,
                null,
                null,
                "$orderBy DESC"
            )?.use { cursor ->
                val idColumn = cursor.getColumnIndex(MediaStore.Audio.Media._ID)
                val idTitle = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
                val idAlbum = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)
                val idDisplayName = cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)
                val idMediaType = cursor.getColumnIndex(MediaStore.Audio.Media.MIME_TYPE)
                cursor.moveToFirst()
                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val uri = ContentUris.withAppendedId(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        id
                    )
                    val albumId = cursor.getLong(idAlbum)
                    val albumUri = ContentUris.withAppendedId(
                        Uri.parse("content://media/external/audio/albumart"),
                        albumId
                    )
                    val title = cursor.getString(idTitle)
                    val displayName = cursor.getString(idDisplayName)
                    val mediaType = cursor.getString(idMediaType)
                    myAudio.add(
                        AudioModel(
                            uri,
                            title,
                            albumUri,
                            displayName,
                            mediaType
                        )
                    )
                }
            }
            _audios.postValue(myAudio)
        }
    }
}