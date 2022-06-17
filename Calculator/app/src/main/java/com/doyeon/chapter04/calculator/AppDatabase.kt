package com.doyeon.chapter04.calculator

import androidx.room.Database
import androidx.room.RoomDatabase
import com.doyeon.chapter04.calculator.dao.HistoryDao
import com.doyeon.chapter04.calculator.model.History

@Database(entities = [History ::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}