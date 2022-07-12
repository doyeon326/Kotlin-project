package com.doyeon.chapter7.airbnb

import retrofit2.Call
import retrofit2.http.GET

interface HouseService {

    @GET("https://run.mocky.io/v3/19e91bcd-3271-4e8d-8be2-5f5d1d88ff29")
    fun getHouseList(): Call<HouseDto>
}