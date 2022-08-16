package com.doyeon.chapter14.deliveryapplication.data.repository.restaurant

import com.doyeon.chapter14.deliveryapplication.data.entity.LocationLatLngEntity
import com.doyeon.chapter14.deliveryapplication.data.entity.RestaurantEntity
import com.doyeon.chapter14.deliveryapplication.screen.main.home.restraurant.RestaurantCategory

interface  RestaurantRepository {

    suspend fun getList (
        //coroutine 으로 구현할거기때문에 suspend 키워드를 사용
        restaurantCategory: RestaurantCategory,
        locationLatLngEntity: LocationLatLngEntity
    ): List<RestaurantEntity>


}