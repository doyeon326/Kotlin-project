package com.doyeon.chapter4.myapplication

import androidx.room.Database
import androidx.room.RoomDatabase
import com.doyeon.chapter4.myapplication.dao.HistoryDao
import com.doyeon.chapter4.myapplication.model.History

@Database(entities = [History::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}