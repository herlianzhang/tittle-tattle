package com.latihangoding.tittle_tattle.api

import com.latihangoding.tittle_tattle.vo.GalleryModel

interface ProgressListener {
    fun onProgress(progress: Float)
    fun onSuccess(gallery: GalleryModel?)
    fun onFail(message: String?)
}
