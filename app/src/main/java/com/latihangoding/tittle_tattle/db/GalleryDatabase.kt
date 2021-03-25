package com.latihangoding.tittle_tattle.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.latihangoding.tittle_tattle.vo.GalleryModel

//data class untuk mengambil data dari database
@Database(entities = [GalleryModel::class], version = 1, exportSchema = false)
abstract class GalleryDatabase : RoomDatabase() {
    abstract fun galleryDao(): GalleryDao
}