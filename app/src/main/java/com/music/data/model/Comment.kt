package com.music.data.model

import com.google.gson.annotations.SerializedName

data class Comment(
    @SerializedName("id")
    val id: String? = null,

    @SerializedName("musicId")
    val musicId: String? = null,

    @SerializedName("content")
    val content: String,

    @SerializedName("userName")
    val userName: String,

    @SerializedName("createdAt")
    val createdAt: String? = null
)