package com.doyeon.chapter14.deliveryapplication.util.provider

import android.content.res.ColorStateList
import androidx.annotation.ColorRes
import androidx.annotation.StringRes

interface ResourcesProvider {
    //참조 .. 리소스 주석을 사용하여 린트와 같은 코드 검사 도구에 힌트를 줌으로써  코드 문제를 예방할 수 있어요
    fun getString(@StringRes resId: Int): String
    fun getString(@StringRes resId: Int, vararg formArgs: Any): String
    fun getColor(@StringRes resId: Int): Int
    fun getColorStateList(@ColorRes resId: Int): ColorStateList
}