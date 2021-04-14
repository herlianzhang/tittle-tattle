package com.latihangoding.tittle_tattle.ui.media

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
//view model untuk fragment media
class MediaViewModel @Inject constructor(app: Application) : AndroidViewModel(app) {
    private val _media = MutableLiveData<List<Uri>>()
    val media: LiveData<List<Uri>>
        get() = _media

    fun setMedia(media: List<Uri>) {
        _media.postValue(media)
    }
}