package com.latihangoding.tittle_tattle.vo

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(val name: String? = "", val email: String? = "", val photoUrl: String? = "", val uid: String? = "")