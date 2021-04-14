package com.latihangoding.tittle_tattle.vo

import android.net.Uri

data class Contact(
    val phoneNumber: String,
    val image: Uri?,
    val email: String?,
    val fullname: String?,
    val firstname: String?,
    val lastname: String?
)