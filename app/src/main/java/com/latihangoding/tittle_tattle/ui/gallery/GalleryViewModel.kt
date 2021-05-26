package com.latihangoding.tittle_tattle.ui.gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.latihangoding.tittle_tattle.repository.GalleryRepository
import com.latihangoding.tittle_tattle.vo.GalleryModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(private val galleryRep: GalleryRepository) :
    ViewModel() {
    val galleryList = galleryRep.getGalleryList

    fun updateGallery(data: GalleryModel) {
        viewModelScope.launch(Dispatchers.IO) {
            galleryRep.updateGallery(data)
        }
    }
}