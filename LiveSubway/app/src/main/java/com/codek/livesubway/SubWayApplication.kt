package com.codek.livesubway

import android.app.Application
import android.util.Log.DEBUG
import com.codek.livesubway.di.appModule
import org.koin.android.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level


class SubWayApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(
                if(BuildConfig.DEBUG) {
                    Level.DEBUG
                } else {
                    Level.NONE
                }
            )
            androidContext(this@SubWayApplication)
            modules(appModule)
        }
    }
}