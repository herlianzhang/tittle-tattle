package com.latihangoding.tittle_tattle.vo

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

//Parcelize berfungsi agar dapat passing data class yang diciptakan namun diperlukan androidExtension Experimental dibuat true
@Parcelize
data class MyModel(val count: Int, val entries: List<Entries>) : Parcelable