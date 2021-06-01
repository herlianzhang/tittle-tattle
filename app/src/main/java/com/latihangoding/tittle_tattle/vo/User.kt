package com.latihangoding.tittle_tattle.vo

import android.os.Parcelable
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize

@IgnoreExtraProperties
@Parcelize
data class User(
    val name: String? = "",
    val email: String? = "",
    val photoUrl: String? = "",
    @Exclude @get:Exclude
    val uid: String? = ""
) : Parcelable