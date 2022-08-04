package com.doyeon.chapter14.deliveryapplication.di

import com.doyeon.chapter14.deliveryapplication.screen.main.home.HomeViewModel
import com.doyeon.chapter14.deliveryapplication.screen.main.home.MyViewModel
import com.doyeon.chapter14.deliveryapplication.util.provider.DefaultResourcesProvider
import com.doyeon.chapter14.deliveryapplication.util.provider.ResourcesProvider
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    //koin DI 셋업
    viewModel { HomeViewModel() }
    viewModel { MyViewModel() }
    single { provideGsonConvertFactory() }
    single { buildOkHttpClient() }

    single { provideRetrofit(get(), get()) }
    single<ResourcesProvider> { DefaultResourcesProvider(androidApplication()) }

    single { Dispatchers.IO }
    single { Dispatchers.Main }

}
