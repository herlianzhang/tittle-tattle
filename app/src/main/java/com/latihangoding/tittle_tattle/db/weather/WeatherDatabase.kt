package com.latihangoding.tittle_tattle.db.weather

import androidx.room.Database
import androidx.room.RoomDatabase
import com.latihangoding.tittle_tattle.vo.Weather

//data class untuk mengambil data dari database
@Database(entities = [Weather::class], version = 1, exportSchema = false)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}