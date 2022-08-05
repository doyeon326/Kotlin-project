package com.doyeon.chapter14.deliveryapplication.data.entity

import kotlinx.android.parcel.Parcelize


interface Entity {
    //parcelize 하기위해 appmodule 에서 의존성 추가
    val id: Long
}