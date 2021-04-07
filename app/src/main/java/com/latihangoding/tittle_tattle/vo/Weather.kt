package com.latihangoding.tittle_tattle.vo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "weather_table")
data class Weather(
    @ColumnInfo(name = "description")
    @SerializedName("description")
    val desc: String?,
    @ColumnInfo(name = "added_date")
    val addedDate: Long = 0
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
