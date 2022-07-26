package com.doyeon.chapter13.environmentpollution.data

import com.doyeon.chapter13.environmentpollution.BuildConfig
import com.doyeon.chapter13.environmentpollution.data.Repository.airKoreaApiService
import com.doyeon.chapter13.environmentpollution.data.models.airquality.MeasuredValue
import com.doyeon.chapter13.environmentpollution.data.models.monitoringstation.MonitoringStation
import com.doyeon.chapter13.environmentpollution.data.services.AirKoreaApiService
import com.doyeon.chapter13.environmentpollution.data.services.kakaoLocalApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object Repository {

    suspend fun getNearbyMonitoringStation(latitude: Double, longitude: Double): MonitoringStation? {
        //위치 변한하기
        val tmCoordinates = kakaoLocalApiService
            .getTmCoordinates(longitude, latitude)
            .body()
            ?.documents
            ?.firstOrNull()

        val tmX = tmCoordinates?.x
        val tmY = tmCoordinates?.y

        //가장 가까운 측정소 가져오기
        return  airKoreaApiService.getNearByMonitoringStation(tmX!!,  tmY!!)
            .body()
            ?.response
            ?.body
            ?.monitoringStations
            ?.minByOrNull { it?.tm ?: Double.MAX_VALUE }
    }

    suspend fun getLatestAirQualityData(station: String): MeasuredValue? =
        airKoreaApiService
            .getRealTimeAirQualities(station)
            .body()
            ?.response
            ?.body
            ?.measuredValue
            ?.firstOrNull()

    private val kakaoLocalApiService: kakaoLocalApiService by lazy {
        Retrofit.Builder()
            .baseUrl(Url.KAKAO_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(buildHttpClient())
            .build()
            .create()
    }

    private val airKoreaApiService: AirKoreaApiService by lazy {
        Retrofit.Builder()
            .baseUrl(Url.AIR_KOREA_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(buildHttpClient())
            .build()
            .create()
    }

    private fun buildHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = if(BuildConfig.DEBUG) {
                        HttpLoggingInterceptor.Level.BODY
                    } else {
                        HttpLoggingInterceptor.Level.NONE
                    }
                }
            ).build()
}