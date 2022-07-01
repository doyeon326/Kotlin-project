package com.doyeon.chapter4.myapplication.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.doyeon.chapter4.myapplication.model.Review

@Dao
interface ReviewDao {

    @Query("SELECT * FROM review WHERE id == :id")
    fun getOneReview(id: Int): Review


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveReview(review: Review)
}