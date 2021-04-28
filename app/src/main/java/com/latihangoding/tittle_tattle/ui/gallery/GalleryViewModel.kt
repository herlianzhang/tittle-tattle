package com.latihangoding.tittle_tattle.ui.gallery

import androidx.lifecycle.ViewModel
import com.latihangoding.tittle_tattle.repository.GalleryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(private val galleryRep: GalleryRepository) :
    ViewModel() {
    val galleryList = galleryRep.getGalleryList
}