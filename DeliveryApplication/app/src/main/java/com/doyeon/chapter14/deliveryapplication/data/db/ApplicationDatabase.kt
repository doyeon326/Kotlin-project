package com.doyeon.chapter14.deliveryapplication.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.doyeon.chapter14.deliveryapplication.data.db.dao.LocationDao
import com.doyeon.chapter14.deliveryapplication.data.entity.LocationLatLngEntity

@Database(
    entities = [LocationLatLngEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ApplicationDatabase: RoomDatabase() {
    companion object {
        const val DB_NAME = "ApplicationDataBase.db"
    }

    abstract fun LocationDao(): LocationDao
}