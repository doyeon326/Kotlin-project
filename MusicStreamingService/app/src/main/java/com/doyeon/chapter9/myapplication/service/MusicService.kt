package com.doyeon.chapter9.myapplication.service

import retrofit2.Call
import retrofit2.http.GET

interface MusicService {

    @GET("/v3/3a55b7e7-d156-42f7-91ab-1406773372d5")
    fun listMusics(): Call<MusicDto>
}