package com.doyeon.chapter13.environmentpollution.data.services

import com.doyeon.chapter13.environmentpollution.BuildConfig
import com.doyeon.chapter13.environmentpollution.data.models.tmcoordinates.TmCoordinatesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface kakaoLocalApiService {

    //카카오좌표계 변환 API: https://developers.kakao.com/docs/latest/ko/local/dev-guide#trans-coord
    @Headers("Authorization: KakaoAK ${BuildConfig.KAKAO_API_KEY}")
    @GET("v2/local/geo/transcoord.json?output_coord=TM")
    suspend fun getTmCoordinates(
        @Query("x")
        longitude: Double,

        @Query("y")
        latitude: Double

        ): Response<TmCoordinatesResponse>
}