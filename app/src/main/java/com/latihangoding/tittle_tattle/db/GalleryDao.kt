package com.latihangoding.tittle_tattle.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.latihangoding.tittle_tattle.vo.GalleryModel

// aksi menyimpan data dengan melakukan perintah sql
@Dao
interface GalleryDao {
    @Query("SELECT * FROM gallery_table ORDER BY added_date DESC")
    fun getAllGallery(): LiveData<List<GalleryModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGallery(gallery: GalleryModel)
}
