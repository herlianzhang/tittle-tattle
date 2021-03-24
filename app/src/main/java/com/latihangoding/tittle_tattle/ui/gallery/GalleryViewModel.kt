package com.latihangoding.tittle_tattle.ui.gallery

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.latihangoding.tittle_tattle.api.ProgressListener
import com.latihangoding.tittle_tattle.repository.GalleryRepository
import com.latihangoding.tittle_tattle.vo.GalleryModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(private val galleryRep: GalleryRepository) : ViewModel(),
    ProgressListener {
    val galleryList = galleryRep.getGalleryList

    fun upload(uri: Uri) {
        viewModelScope.launch {
            galleryRep.upload(uri, this@GalleryViewModel)
        }
    }

    override fun onProgress(progress: Float) {
        Timber.d("Masuk progress $progress")
    }

    override fun onSuccess(gallery: GalleryModel?) {
        Timber.d("Masuk success $gallery")
    }

    override fun onFail(message: String?) {
        Timber.e("Masuk fail $message")
    }
}