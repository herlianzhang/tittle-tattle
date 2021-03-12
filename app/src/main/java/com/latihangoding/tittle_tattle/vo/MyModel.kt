package com.latihangoding.tittle_tattle.vo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

//Parcelize berfungsi agar dapat passing data class yang diciptakan namun diperlukan androidExtension Experimental dibuat true
@Parcelize
data class MyModel(val message: String) : Parcelable