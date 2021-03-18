package com.latihangoding.tittle_tattle.repository

import com.latihangoding.tittle_tattle.api.ApiService
import com.latihangoding.tittle_tattle.vo.getResult
import javax.inject.Inject

class HomeRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getEntries() = getResult {
        // ngambil data dari REST API menggunakan library retrofit
        apiService.getEntries()
    }
}