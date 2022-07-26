package com.doyeon.chapter13.environmentpollution.data.services

import com.doyeon.chapter13.environmentpollution.BuildConfig
import com.doyeon.chapter13.environmentpollution.data.models.airquality.AirQualityResponse
import com.doyeon.chapter13.environmentpollution.data.models.monitoringstation.MonitoringStationsResponse
import retrofit2.Response
import retrofit2.http.GET

import retrofit2.http.Query

interface AirKoreaApiService {
   // http://apis.data.go.kr
    @GET("B552584/MsrstnInfoInqireSvc/getNearbyMsrstnList" +
            "?serviceKey=${BuildConfig.AIR_KOREA_SERVICE_KEY}" +
            "&returnType=json")
    suspend fun getNearByMonitoringStation(
        //https://www.data.go.kr/iim/api/selectAPIAcountView.do
        @Query("tmX")
        tmX: Double,

        @Query("tmY")
        tmY: Double

    ): Response<MonitoringStationsResponse>

    @GET("B552584/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty" +
            "?serviceKey=${BuildConfig.AIR_KOREA_SERVICE_KEY}"+
            "&returnType=json" +
            "&dataTerm=DAILY" +
            "&ver=1.3"
         )
    suspend fun getRealTimeAirQualities(

        @Query("stationName")
        stationName: String

    ):Response<AirQualityResponse>

}