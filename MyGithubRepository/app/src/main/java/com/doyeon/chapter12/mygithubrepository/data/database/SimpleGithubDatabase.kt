package com.doyeon.chapter12.mygithubrepository.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.doyeon.chapter12.mygithubrepository.data.dao.RepositoryDao
import com.doyeon.chapter12.mygithubrepository.data.entity.GithubRepoEntity

@Database(entities = [GithubRepoEntity::class], version = 1)
abstract class SimpleGithubDatabase: RoomDatabase() {

    abstract fun repositoryDao(): RepositoryDao
}