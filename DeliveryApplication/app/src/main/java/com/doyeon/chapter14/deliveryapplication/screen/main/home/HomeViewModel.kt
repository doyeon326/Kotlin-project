package com.doyeon.chapter14.deliveryapplication.screen.main.home

import androidx.lifecycle.MutableLiveData
import com.doyeon.chapter14.deliveryapplication.screen.base.BaseViewModel

class HomeViewModel: BaseViewModel() {
    val homeSateLiveData = MutableLiveData<HomeState>(HomeState.Uninitialized) //현재 위치에 대한 값
}