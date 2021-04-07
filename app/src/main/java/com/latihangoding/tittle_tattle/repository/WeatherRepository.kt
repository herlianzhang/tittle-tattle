package com.latihangoding.tittle_tattle.repository

import com.latihangoding.tittle_tattle.api.ApiService
import com.latihangoding.tittle_tattle.db.weather.WeatherDao
import com.latihangoding.tittle_tattle.di.WeatherApi
import com.latihangoding.tittle_tattle.vo.Weather
import com.latihangoding.tittle_tattle.vo.getResult
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val weatherDao: WeatherDao,
    @WeatherApi private val apiService: ApiService
) {
    val getAllWeather = weatherDao.getAllWeather()

    fun insertWeather(weather: Weather) {
        weatherDao.insertWeather(weather)
    }

//    menyimpan api key dan city medan
    suspend fun fetchWeather() = getResult {
        apiService.getWeather(
            city = "Medan",
            appId = "3d4663fc3b3523d7500bb42e34ed47de"
        )
    }
}
