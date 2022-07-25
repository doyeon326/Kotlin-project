package com.doyeon.chapter12.mygithubrepository

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isGone
import com.doyeon.chapter12.mygithubrepository.data.database.DataBaseProvider
import com.doyeon.chapter12.mygithubrepository.data.entity.GithubOwner
import com.doyeon.chapter12.mygithubrepository.data.entity.GithubRepoEntity
import com.doyeon.chapter12.mygithubrepository.data.view.RepositoryRecyclerAdapter
import com.doyeon.chapter12.mygithubrepository.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import java.util.*
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {

    val job = Job()
    private val repositoryDao by lazy {
        DataBaseProvider.provideDB(applicationContext).repositoryDao()
    }
    private lateinit var binding: ActivityMainBinding

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job


    private lateinit var adapter: RepositoryRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initAdapter()
        initViews()

    }

    private fun initAdapter() {

        adapter = RepositoryRecyclerAdapter()
    }
    private fun initViews() = with(binding){
        recyclerView.adapter = adapter
        searchButton.setOnClickListener {
            startActivity(Intent(this@MainActivity, SearchActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        launch(coroutineContext) {
            loadLikeRepositoryList()
        }

    }

    suspend fun loadLikeRepositoryList() = withContext(Dispatchers.IO) {
        val repoList = repositoryDao.getHistory()
        withContext(Dispatchers.Main) {
            setData(repoList)
        }
    }


    private fun setData(githubRepositoryList: List<GithubRepoEntity>)= with(binding) {
        if(githubRepositoryList.isEmpty()) {
            emptyResultTextView.isGone = false
            recyclerView.isGone = true

        } else {
            emptyResultTextView.isGone = true
            recyclerView.isGone = false
            adapter.setRepositoryList(githubRepositoryList) {
                startActivity(Intent(
                    this@MainActivity, RepositoryActivity::class.java
                ).apply {
                    putExtra(RepositoryActivity.REPOSITORY_NAME_KEY, it.name)
                    putExtra(RepositoryActivity.REPOSITORY_OWNER_KEY, it.owner.login)

                }
                )
            }
        }
    }

//    private suspend fun addMockData() = withContext(Dispatchers.IO) {
//        val mocData =  (0 until 10).map {
//            GithubRepoEntity(
//                name = "repo $it",
//                fullName = "name $it",
//                owner = GithubOwner(
//                    "login",
//                    "avatarUrl"
//                ),
//                description = null,
//                language = null,
//                updatedAt = Date().toString(),
//                stargazersCount = it
//            )
//        }
//        repositoryDao.insertAll(mocData)
//    }


}