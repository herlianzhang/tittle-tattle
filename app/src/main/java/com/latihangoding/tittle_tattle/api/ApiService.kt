package com.latihangoding.tittle_tattle.api

import com.latihangoding.tittle_tattle.vo.GalleryModel
import com.latihangoding.tittle_tattle.vo.MyModel
import com.latihangoding.tittle_tattle.vo.WeatherApiModel
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @GET("entries")
    suspend fun getEntries(): Response<MyModel>

    @POST("upload")
    suspend fun uploadImage(@Body body: RequestBody): Response<GalleryModel>

    @GET("weather")
    suspend fun getWeather(@Query("q") city: String, @Query("appid") appId: String): Response<WeatherApiModel>
}
