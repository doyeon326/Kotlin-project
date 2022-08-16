package com.doyeon.chapter14.deliveryapplication.di

import android.content.Context
import androidx.room.Room
import com.doyeon.chapter14.deliveryapplication.data.db.ApplicationDatabase

fun provideDB(context: Context): ApplicationDatabase =
    Room.databaseBuilder(context, ApplicationDatabase::class.java, ApplicationDatabase.DB_NAME).build()

fun provideLocationDao(database: ApplicationDatabase) = database.LocationDao()
