package com.doyeon.chapter14.deliveryapplication.data.entity

import android.os.Parcelable
import com.doyeon.chapter14.deliveryapplication.screen.main.home.restraurant.RestaurantCategory
import kotlinx.android.parcel.Parcelize


@Parcelize
class RestaurantEntity(
    override val id: Long,
    val restaurantInfoId: Long,
    val restaurantCategory: RestaurantCategory,
    val restaurantTitle: String,
    val restaurantImageUrl: String,
    val grade: Float,
    val reviewCount: Int,
    val deliveryTimeRange: Pair<Int,Int>,
    val deliveryTipRange: Pair<Int, Int>

): Entity, Parcelable