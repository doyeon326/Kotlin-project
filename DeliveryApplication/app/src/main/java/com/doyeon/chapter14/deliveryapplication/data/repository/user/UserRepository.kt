package com.doyeon.chapter14.deliveryapplication.data.repository.user

import com.doyeon.chapter14.deliveryapplication.data.entity.LocationLatLngEntity

interface UserRepository {
    suspend fun getUserLocation(): LocationLatLngEntity?
    suspend fun insertUserLocation(locationLatLngEntity: LocationLatLngEntity)
}