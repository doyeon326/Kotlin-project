package com.doyeon.chapter9.myapplication.service

import com.google.gson.annotations.SerializedName

data class MusicEntity (
    @SerializedName("tracks") val track: String,
    @SerializedName("streamUrl") val streamUrl: String,
    @SerializedName("artist") val artist: String,
    @SerializedName("coverUrl") val coverUrl: String
        )