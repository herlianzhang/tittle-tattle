package com.latihangoding.tittle_tattle.api

import com.latihangoding.tittle_tattle.vo.MyModel
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("entries")
    suspend fun getEntries(): Response<MyModel>
}
