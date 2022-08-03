package com.doyeon.chapter14.deliveryapplication

import android.app.Application
import android.content.Context
import com.doyeon.chapter14.deliveryapplication.di.appModule
import org.koin.core.context.startKoin
import org.koin.dsl.module

class Chapter14DeliveryApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = this

        startKoin { modules(appModule) }
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