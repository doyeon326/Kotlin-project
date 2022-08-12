package com.doyeon.chapter14.deliveryapplication.data.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MapSearchInfoEntity(
    val fullAddress: String,
    val name: String,
    val locationLatLng: LocationLatLngEntity
): Parcelable