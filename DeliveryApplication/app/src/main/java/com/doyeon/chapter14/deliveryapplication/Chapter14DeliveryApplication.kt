package com.doyeon.chapter14.deliveryapplication

import android.app.Application
import android.content.Context

class Chapter14DeliveryApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = this
    }

    override fun onTerminate() {
        super.onTerminate()
        appContext = null

    }

    companion object {
        var appContext: Context? = null
            private set
    }

}