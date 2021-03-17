package com.latihangoding.tittle_tattle.vo

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Entries(
    @field:SerializedName("API")
    val name: String,
    @field:SerializedName("Description")
    val desc: String
) : Parcelable