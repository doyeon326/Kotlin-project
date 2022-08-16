package com.doyeon.chapter14.deliveryapplication.screen.main.home

import androidx.annotation.StringRes
import com.doyeon.chapter14.deliveryapplication.data.entity.MapSearchInfoEntity

sealed class HomeState {
    object Uninitialized: HomeState()

    object Loading: HomeState()

 //   object Success: HomeState() // data class 는 무조건 하나 이상의 프로퍼티를 가져야한다.
    data class Success(
       val mapSearchInfoEntity: MapSearchInfoEntity,
       val isLocationSame: Boolean
    ): HomeState()

    data class Error(
        @StringRes val messageId: Int
    ): HomeState()
}
