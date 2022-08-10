package com.doyeon.chapter14.deliveryapplication.data.repository.map

import android.util.Log
import com.doyeon.chapter14.deliveryapplication.data.entity.LocationLatLngEntity
import com.doyeon.chapter14.deliveryapplication.data.network.MapApiService
import com.doyeon.chapter14.deliveryapplication.data.response.address.AddressInfo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultMapRepository(
    private val mapApiService: MapApiService,
    private val ioDispatcher: CoroutineDispatcher
): MapRepository {


    override suspend fun getReverseGeoInformation(
        locationLatLngEntity: LocationLatLngEntity)
    : AddressInfo? = withContext(ioDispatcher) {
       val response = mapApiService.getReverseGeoCode(
            lat = locationLatLngEntity.latitude,
            lon = locationLatLngEntity.longitude
        )


        if (response.isSuccessful) {

            response.body()?.addressInfo
        } else {
            null
        }
    }
}