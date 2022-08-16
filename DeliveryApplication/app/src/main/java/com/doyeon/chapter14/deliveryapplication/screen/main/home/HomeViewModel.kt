package com.doyeon.chapter14.deliveryapplication.screen.main.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.doyeon.chapter14.deliveryapplication.R
import com.doyeon.chapter14.deliveryapplication.data.entity.LocationLatLngEntity
import com.doyeon.chapter14.deliveryapplication.data.entity.MapSearchInfoEntity
import com.doyeon.chapter14.deliveryapplication.data.repository.map.MapRepository
import com.doyeon.chapter14.deliveryapplication.data.repository.user.UserRepository
import com.doyeon.chapter14.deliveryapplication.screen.base.BaseViewModel
import kotlinx.coroutines.launch

class HomeViewModel(
    private val mapRepository: MapRepository,
    private val userRepository: UserRepository
    ): BaseViewModel() {

    companion object {
        const val MY_LOCATION_KEY = "myLocation"
    }
    val homeSateLiveData = MutableLiveData<HomeState>(HomeState.Uninitialized) //현재 위치에 대한 값

    fun loadReverseGeoInformation(
        locationLatLngEntity: LocationLatLngEntity
    ) = viewModelScope.launch{
        homeSateLiveData.value = HomeState.Loading

        val userLocation = userRepository.getUserLocation()
        val currentLocation = userLocation ?: locationLatLngEntity
       val addressInfo = mapRepository.getReverseGeoInformation(currentLocation)

        addressInfo?.let { info ->

            homeSateLiveData.value = HomeState.Success(
                mapSearchInfoEntity = info.toSearchInfoEntity(locationLatLngEntity),
                isLocationSame =  currentLocation == locationLatLngEntity
            )

        }?: kotlin.run {
            homeSateLiveData.value = HomeState.Error(
                R.string.can_not_load_address_info
            )
        }
    }

    fun getMapSearchInfo(): MapSearchInfoEntity? {
        when (val data = homeSateLiveData.value) {
            is HomeState.Success -> {
                return data.mapSearchInfoEntity
            }
        }
        return null
    }

}