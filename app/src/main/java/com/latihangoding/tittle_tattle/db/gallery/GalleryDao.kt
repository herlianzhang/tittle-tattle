package com.latihangoding.tittle_tattle.db.gallery

import androidx.lifecycle.LiveData
import androidx.room.*
import com.latihangoding.tittle_tattle.vo.GalleryModel

// aksi menyimpan data dengan melakukan perintah sql
@Dao
interface GalleryDao {
    @Query("SELECT * FROM gallery_table ORDER BY added_date DESC")
    fun getAllGallery(): LiveData<List<GalleryModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGallery(gallery: GalleryModel)

    @Update
    fun updateGallery(gallery: GalleryModel)
}
