package com.doyeon.chapter14.deliveryapplication.data.repository.map

import com.doyeon.chapter14.deliveryapplication.data.entity.LocationLatLngEntity
import com.doyeon.chapter14.deliveryapplication.data.response.address.AddressInfo

interface MapRepository {

    suspend fun getReverseGeoInformation(locationLatLngEntity: LocationLatLngEntity) : AddressInfo?
}