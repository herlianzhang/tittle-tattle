package com.latihangoding.tittle_tattle.vo

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Chat(
    val messageText: String? = "",
    val receiverId: String? = "",
    val senderId: String? = "",
    @Exclude @get:Exclude
    val timestamp: String? = "",
    @Exclude @get:Exclude
    val isPending: Boolean = false
)