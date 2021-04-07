package com.latihangoding.tittle_tattle.vo

import com.google.gson.annotations.SerializedName

data class WeatherApiModel(
    @SerializedName("weather")
    val weathers: List<Weather>
)
