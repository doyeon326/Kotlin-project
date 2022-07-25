package com.doyeon.chapter12.mygithubrepository

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.doyeon.chapter12.mygithubrepository.data.database.DataBaseProvider
import com.doyeon.chapter12.mygithubrepository.data.entity.GithubOwner
import com.doyeon.chapter12.mygithubrepository.data.entity.GithubRepoEntity
import com.doyeon.chapter12.mygithubrepository.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import java.util.*
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {

    val job = Job()
    val repositoryDao by lazy {
        DataBaseProvider.provideDB(applicationContext).repositoryDao()
    }
    private lateinit var binding: ActivityMainBinding

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        launch {
            addMockData()
            val gitHubRepositories = loadGithubRepositories()

            withContext(coroutineContext) {
                Log.e("repositories", gitHubRepositories.toString())
            }
        }
        initViews()

    }

    private fun initViews() = with(binding){
        searchButton.setOnClickListener {
            startActivity(Intent(this@MainActivity, SearchActivity::class.java))
        }
    }

    private suspend fun loadGithubRepositories() = withContext(Dispatchers.IO) {
        val repositories = repositoryDao.getHistory()
        return@withContext repositories

    }

    private suspend fun addMockData() = withContext(Dispatchers.IO) {
        val mocData =  (0 until 10).map {
            GithubRepoEntity(
                name = "repo $it",
                fullName = "name $it",
                owner = GithubOwner(
                    "login",
                    "avatarUrl"
                ),
                description = null,
                language = null,
                updatedAt = Date().toString(),
                stargazersCount = it
            )
        }
        repositoryDao.insertAll(mocData)
    }


}