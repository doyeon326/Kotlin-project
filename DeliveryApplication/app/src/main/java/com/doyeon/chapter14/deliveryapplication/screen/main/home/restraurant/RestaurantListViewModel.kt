package com.doyeon.chapter14.deliveryapplication.screen.main.home.restraurant

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.doyeon.chapter14.deliveryapplication.data.repository.restaurant.RestaurantRepository
import com.doyeon.chapter14.deliveryapplication.model.restaurant.RestaurantModel
import com.doyeon.chapter14.deliveryapplication.screen.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RestaurantListViewModel(
    private val restaurantCategory: RestaurantCategory,
    private val restaurantRepository: RestaurantRepository

    ): BaseViewModel() {


    val restaurantListLiveData = MutableLiveData<List<RestaurantModel>>()

    override fun fetchData(): Job = viewModelScope.launch{

        val restaurantList = restaurantRepository.getList(restaurantCategory)
        restaurantListLiveData.value = restaurantList.map {
            RestaurantModel(
                id = it.id,
                restaurantInfoId = it.restaurantInfoId,
                restaurantCategory = it.restaurantCategory,
                restaurantImageUrl = it.restaurantImageUrl,
                restaurantTitle = it.restaurantTitle,
                grade = it.grade,
                reviewCount = it.reviewCount,
                deliveryTimeRange = it.deliveryTimeRange,
                deliveryTipRange = it.deliveryTipRange

            )
        }
    }
}