package com.doyeon.chapter14.deliveryapplication.screen.main.home.restaurant.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.doyeon.chapter14.deliveryapplication.data.entity.RestaurantEntity
import com.doyeon.chapter14.deliveryapplication.screen.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RestaurantDetailViewModel(
    private val restaurantEntity: RestaurantEntity
) : BaseViewModel() {
    val restaurantDetailLiveData = MutableLiveData<RestaurantDetailState>(RestaurantDetailState.Uninitialized)
    override fun fetchData(): Job = viewModelScope.launch {
        restaurantDetailLiveData.value = RestaurantDetailState.Success(
            restaurantEntity = restaurantEntity
        )
    }
}