package com.doyeon.chapter14.deliveryapplication.screen.main.home.restraurant

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.doyeon.chapter14.deliveryapplication.data.entity.RestaurantEntity
import com.doyeon.chapter14.deliveryapplication.data.repository.RestaurantRepository
import com.doyeon.chapter14.deliveryapplication.screen.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RestaurantListViewModel(
    private val restaurantCategory: RestaurantCategory,
    private val restaurantRepository: RestaurantRepository

    ): BaseViewModel() {


    val restaurantListLiveData = MutableLiveData<List<RestaurantEntity>>()

    override fun fetchData(): Job = viewModelScope.launch{

        val restaurantList = restaurantRepository.getList(restaurantCategory)
        Log.d("RestaurantListViewModel()" ,"${restaurantList.size}")
        restaurantListLiveData.value = restaurantList
    }
}