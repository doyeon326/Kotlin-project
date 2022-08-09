package com.doyeon.chapter14.deliveryapplication.extensions

import android.content.res.Resources

fun Float.fromDpToPx(): Int {
    return (this * Resources.getSystem().displayMetrics.density).toInt()
}