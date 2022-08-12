package com.doyeon.chapter14.deliveryapplication.screen.mylocation

import androidx.annotation.StringRes
import com.doyeon.chapter14.deliveryapplication.data.entity.MapSearchInfoEntity
import com.doyeon.chapter14.deliveryapplication.screen.main.home.HomeState

sealed class MyLocationState {
    object Uninitialized: MyLocationState()

    object Loading: MyLocationState()

    data class Success(
        val mapSearchInfoEntity: MapSearchInfoEntity
    ): MyLocationState()

    data class Confirm(
        val mapSearchInfoEntity: MapSearchInfoEntity
    ): MyLocationState()

    data class Error(
        @StringRes val messageId: Int

    ): MyLocationState()
}