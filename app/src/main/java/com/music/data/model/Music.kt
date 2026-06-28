package com.music.data.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class Music(
    @SerializedName("id")
    val id: String? = null,

    @SerializedName("title")
    val title: String,

    @SerializedName("artist")
    val artist: String,

    @SerializedName("album")
    val album: String? = null,

    @SerializedName("genre")
    val genre: String? = null,

    @SerializedName("year")
    val year: Int? = null,

    @SerializedName("comments")
    val comments: List<Comment> = emptyList(),

    @SerializedName("createdAt")
    val createdAt: String? = null,

    @SerializedName("updatedAt")
    val updatedAt: String? = null
)