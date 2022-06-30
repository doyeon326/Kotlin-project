package com.doyeon.chapter4.myapplication.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.doyeon.chapter4.myapplication.model.History

@Dao
interface HistoryDao {

    @Query("SELECT * FROM history")
    fun getAll(): List<History>

    @Insert
    fun insertHistory(history: History)

    @Query("DELETE FROM history WHERE keyword == :keyword")
    fun delete(keyword: String)
}