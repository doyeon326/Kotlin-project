package com.doyeon.chapter14.deliveryapplication.screen.main.home

sealed class HomeState {
    object Uninitialized: HomeState()

    object Loading: HomeState()

    object Success: HomeState() // data class 는 무조건 하나 이상의 프로퍼티를 가져야한다.
}
