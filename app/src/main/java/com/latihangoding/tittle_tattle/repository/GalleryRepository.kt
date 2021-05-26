package com.latihangoding.tittle_tattle.repository

import android.content.Context
import android.net.Uri
import com.latihangoding.tittle_tattle.api.ApiService
import com.latihangoding.tittle_tattle.api.InputStreamRequestBody
import com.latihangoding.tittle_tattle.api.ProgressListener
import com.latihangoding.tittle_tattle.db.gallery.GalleryDao
import com.latihangoding.tittle_tattle.di.UploadApi
import com.latihangoding.tittle_tattle.vo.GalleryModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import java.lang.Exception
import javax.inject.Inject

class GalleryRepository @Inject constructor(
    private val galleryDao: GalleryDao,
    @UploadApi private val apiService: ApiService,
    @ApplicationContext private val context: Context
) {
    private var job: Job? = null

    //    mengambil list data dari database
    val getGalleryList = galleryDao.getAllGallery()

    //    melakukan upload file dari gallery ke database menggunakan REST API, untuk menyimpan data yang telah diupload ke apk
    suspend fun upload(uri: Uri, listener: ProgressListener) {
        val progressListener = InputStreamRequestBody(
            contentType = "image/jpg".toMediaTypeOrNull(),
            contentResolver = context.contentResolver,
            uri = uri,
            context = context,
            listener
        )

        val body = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(
                "File",
                System.currentTimeMillis().toString(),
                progressListener
            )
            .addFormDataPart(
                "Type_Adaptor",
                "GDRIVE"
            )
            .build()

        job?.cancel()
        job = Job()

        CoroutineScope(job as CompletableJob + Dispatchers.IO).launch {
            try {
                val response = apiService.uploadImage(body)
                if (response.isSuccessful) {
                    val data = response.body()
                    listener.onSuccess(data)
                } else {
                    throw Exception(response.message())
                }
            } catch (e: Exception) {
                listener.onFail(e.message)
            }
        }
    }

    fun cancleUpload() {
        job?.cancel()
        job = null
    }

    //masukkan gallery ke database
    fun insertGallery(data: GalleryModel) {
        galleryDao.insertGallery(data)
    }

    suspend fun updateGallery(data: GalleryModel) {
        galleryDao.updateGallery(data)
    }
}
