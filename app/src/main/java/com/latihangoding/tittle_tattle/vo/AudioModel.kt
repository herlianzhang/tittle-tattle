package com.latihangoding.tittle_tattle.vo

import android.net.Uri

//Dataclass untuk Audio Model
data class AudioModel(
    val uri: Uri,
    val title: String?,
    val albumUri: Uri,
    val displayName: String?,
    val mediaType: String?
)