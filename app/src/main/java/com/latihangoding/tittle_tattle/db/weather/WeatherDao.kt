package com.latihangoding.tittle_tattle.db.weather

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.latihangoding.tittle_tattle.vo.Weather

@Dao
interface WeatherDao {
    @Query("SELECT * FROM weather_table ORDER BY added_date DESC")
    fun getAllWeather(): LiveData<List<Weather>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWeather(weather: Weather)
}