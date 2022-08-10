package com.doyeon.chapter14.deliveryapplication.data.entity

data class MapSearchInfoEntity(
    val fullAddress: String,
    val name: String,
    val locationLatLng: LocationLatLngEntity
)