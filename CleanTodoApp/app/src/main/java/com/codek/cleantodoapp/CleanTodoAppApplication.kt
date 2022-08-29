package com.codek.cleantodoapp

import android.app.Application
import com.codek.cleantodoapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class CleanTodoAppApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        //TODO Koin Trigger
        /*
        * 1. Manifest 에 Application 등록 -> manifest line@15
        * 2. 코인 의존성 추가 gradle
        * 3. 코인 모듈 생성후  아래 modules() 에 추가
        * */
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@CleanTodoAppApplication)
            modules(appModule) //Koin Module
        }

    }
}