package com.doyeon.chapter14.deliveryapplication.widget.adapter.listener

import com.doyeon.chapter14.deliveryapplication.model.restaurant.RestaurantModel

interface RestaurantListListener: AdapterListener {

    fun oncClickItem(model: RestaurantModel)
}