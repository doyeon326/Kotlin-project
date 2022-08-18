package com.doyeon.chapter14.deliveryapplication.di

import com.doyeon.chapter14.deliveryapplication.data.entity.LocationLatLngEntity
import com.doyeon.chapter14.deliveryapplication.data.entity.MapSearchInfoEntity
import com.doyeon.chapter14.deliveryapplication.data.entity.RestaurantEntity
import com.doyeon.chapter14.deliveryapplication.data.repository.map.DefaultMapRepository
import com.doyeon.chapter14.deliveryapplication.data.repository.map.MapRepository
import com.doyeon.chapter14.deliveryapplication.data.repository.restaurant.DefaultRestaurantRepository
import com.doyeon.chapter14.deliveryapplication.data.repository.restaurant.RestaurantRepository
import com.doyeon.chapter14.deliveryapplication.data.repository.user.DefaultUserRepository
import com.doyeon.chapter14.deliveryapplication.data.repository.user.UserRepository
import com.doyeon.chapter14.deliveryapplication.screen.main.home.HomeViewModel
import com.doyeon.chapter14.deliveryapplication.screen.main.home.MyViewModel
import com.doyeon.chapter14.deliveryapplication.screen.main.home.restaurant.RestaurantCategory
import com.doyeon.chapter14.deliveryapplication.screen.main.home.restaurant.RestaurantListViewModel
import com.doyeon.chapter14.deliveryapplication.screen.main.home.restaurant.detail.RestaurantDetailViewModel
import com.doyeon.chapter14.deliveryapplication.screen.mylocation.MyLocationViewModel
import com.doyeon.chapter14.deliveryapplication.util.provider.DefaultResourcesProvider
import com.doyeon.chapter14.deliveryapplication.util.provider.ResourcesProvider
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    //koin DI 셋업
    viewModel { HomeViewModel(get(), get()) }
    viewModel { MyViewModel() }
    viewModel { (restaurantCategory: RestaurantCategory, locationLatLng: LocationLatLngEntity) -> RestaurantListViewModel( restaurantCategory , locationLatLng, get())}
    viewModel { (mapsSearchInfoEntity: MapSearchInfoEntity) -> MyLocationViewModel(mapsSearchInfoEntity, get(), get())}
    viewModel { (restaurantEntity: RestaurantEntity) -> RestaurantDetailViewModel(restaurantEntity)}
    single<RestaurantRepository> { DefaultRestaurantRepository(get(), get(), get())  }
    single<MapRepository> { DefaultMapRepository(get(), get())}
    single<UserRepository> { DefaultUserRepository(get(), get())  }

    single { provideGsonConvertFactory() }
    single { buildOkHttpClient() }

    single { provideMapRetrofit(get(), get()) }

    single { provideMapApiService(get()) }

    single { provideDB(androidApplication()) }
    single { provideLocationDao(get()) }

    single<ResourcesProvider> { DefaultResourcesProvider(androidApplication()) }

    single { Dispatchers.IO }
    single { Dispatchers.Main }

}
