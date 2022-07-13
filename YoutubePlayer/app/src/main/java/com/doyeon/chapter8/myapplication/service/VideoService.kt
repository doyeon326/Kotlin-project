package com.doyeon.chapter8.myapplication.service

import com.doyeon.chapter8.myapplication.dto.VideoDto
import retrofit2.Call
import retrofit2.http.GET

interface VideoService {
    @GET("/v3/7848cf1d-9959-4598-b7bb-5e57d76876b5")
    fun listVideo(): Call<VideoDto>
}