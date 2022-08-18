package com.doyeon.chapter14.deliveryapplication.model.restaurant

import com.doyeon.chapter14.deliveryapplication.data.entity.RestaurantEntity
import com.doyeon.chapter14.deliveryapplication.model.CellType
import com.doyeon.chapter14.deliveryapplication.model.Model
import com.doyeon.chapter14.deliveryapplication.screen.main.home.restaurant.RestaurantCategory

data class RestaurantModel(
    override val id: Long,
    override val type: CellType = CellType.RESTAURANT_CELL,
    val restaurantInfoId: Long,
    val restaurantCategory: RestaurantCategory,
    val restaurantTitle: String,
    val restaurantImageUrl: String,
    val grade: Float,
    val reviewCount: Int,
    val deliveryTimeRange: Pair<Int,Int>,
    val deliveryTipRange: Pair<Int, Int>,
    val restaurantTelNum: String?
): Model(id, type){
    fun toEntity() = RestaurantEntity(
        id,
        restaurantInfoId,
        restaurantCategory,
        restaurantTitle,
        restaurantImageUrl,
        grade,
        reviewCount,
        deliveryTimeRange,
        deliveryTipRange,
        restaurantTelNum
    )
}