package com.doyeon.chapter14.deliveryapplication.screen.main.home.restaurant

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.doyeon.chapter14.deliveryapplication.data.entity.LocationLatLngEntity
import com.doyeon.chapter14.deliveryapplication.data.repository.restaurant.RestaurantRepository
import com.doyeon.chapter14.deliveryapplication.model.restaurant.RestaurantModel
import com.doyeon.chapter14.deliveryapplication.screen.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RestaurantListViewModel(
    private val restaurantCategory: RestaurantCategory,
    private var locationLatLng: LocationLatLngEntity,
    private val restaurantRepository: RestaurantRepository,
    private var restaurantOrder: RestaurantOrder = RestaurantOrder.DEFAULT

    ): BaseViewModel() {


    val restaurantListLiveData = MutableLiveData<List<RestaurantModel>>()

    override fun fetchData(): Job = viewModelScope.launch{
        val restaurantList = restaurantRepository.getList(restaurantCategory, locationLatLng)
        val sortedList = when (restaurantOrder) {
            RestaurantOrder.TOP_LATE -> {
                restaurantList.sortedByDescending { it.grade }
            }
            RestaurantOrder.DEFAULT -> {
                restaurantList
            }
            RestaurantOrder.LOW_DELIVERY_TIP -> {
                restaurantList.sortedBy { it.deliveryTipRange.first }
            }
            RestaurantOrder.FAST_DELIVERY -> {
                restaurantList.sortedBy { it.deliveryTimeRange.first }
            }
        }
        restaurantListLiveData.value = sortedList.map {
            RestaurantModel(
                id = it.id,
                restaurantInfoId = it.restaurantInfoId,
                restaurantCategory = it.restaurantCategory,
                restaurantImageUrl = it.restaurantImageUrl,
                restaurantTitle = it.restaurantTitle,
                grade = it.grade,
                reviewCount = it.reviewCount,
                deliveryTimeRange = it.deliveryTimeRange,
                deliveryTipRange = it.deliveryTipRange,
                restaurantTelNum = it.restaurantTelNum

            )
        }
    }
    fun setLocationLatLng(locationLatLng: LocationLatLngEntity) {
        this.locationLatLng = locationLatLng
        fetchData()
    }

    fun setRestaurantFilterOrder(order: RestaurantOrder) {
        this.restaurantOrder = order
        fetchData()
    }
}