package com.doyeon.chapter14.deliveryapplication.screen.main.home.restraurant

import androidx.annotation.StringRes
import com.doyeon.chapter14.deliveryapplication.R

enum class RestaurantCategory(
    @StringRes val categoryNameId: Int,
    @StringRes val categoryType: Int
) {
    ALL(R.string.all, R.string.all_type)
}