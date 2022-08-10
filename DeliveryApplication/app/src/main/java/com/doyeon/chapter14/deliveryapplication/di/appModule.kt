package com.doyeon.chapter14.deliveryapplication.di

import com.doyeon.chapter14.deliveryapplication.data.repository.map.DefaultMapRepository
import com.doyeon.chapter14.deliveryapplication.data.repository.map.MapRepository
import com.doyeon.chapter14.deliveryapplication.data.repository.restaurant.DefaultRestaurantRepository
import com.doyeon.chapter14.deliveryapplication.data.repository.restaurant.RestaurantRepository
import com.doyeon.chapter14.deliveryapplication.screen.main.home.HomeViewModel
import com.doyeon.chapter14.deliveryapplication.screen.main.home.MyViewModel
import com.doyeon.chapter14.deliveryapplication.screen.main.home.restraurant.RestaurantCategory
import com.doyeon.chapter14.deliveryapplication.screen.main.home.restraurant.RestaurantListViewModel
import com.doyeon.chapter14.deliveryapplication.util.provider.DefaultResourcesProvider
import com.doyeon.chapter14.deliveryapplication.util.provider.ResourcesProvider
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    //koin DI 셋업
    viewModel { HomeViewModel(get()) }
    viewModel { MyViewModel() }
    viewModel { (restaurantCategory: RestaurantCategory) -> RestaurantListViewModel( restaurantCategory , get())}

    single<RestaurantRepository> { DefaultRestaurantRepository(get(), get())  }
    single<MapRepository> { DefaultMapRepository(get(), get())}

    single { provideGsonConvertFactory() }
    single { buildOkHttpClient() }

    single { provideMapRetrofit(get(), get()) }

    single { provideMapApiService(get()) }

    single<ResourcesProvider> { DefaultResourcesProvider(androidApplication()) }

    single { Dispatchers.IO }
    single { Dispatchers.Main }

}
