package com.latihangoding.tittle_tattle.repository

import com.latihangoding.tittle_tattle.api.ApiService
import com.latihangoding.tittle_tattle.vo.getResult
import javax.inject.Inject

//Model: Ini menyimpan data aplikasi. Itu tidak bisa langsung berbicara dengan View.
// Secara umum, disarankan untuk mengekspos data ke ViewModel melalui Observables.

class HomeRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getEntries() = getResult {
        apiService.getEntries()
    }
}