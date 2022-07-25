package com.doyeon.chapter12.mygithubrepository

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.view.isGone
import com.doyeon.chapter12.mygithubrepository.data.entity.GithubRepoEntity
import com.doyeon.chapter12.mygithubrepository.data.view.RepositoryRecyclerAdapter
import com.doyeon.chapter12.mygithubrepository.databinding.ActivitySearchBinding
import com.doyeon.chapter12.mygithubrepository.utility.RetrofitUtil
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class SearchActivity : AppCompatActivity(), CoroutineScope {

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job


    private lateinit var binding: ActivitySearchBinding
    private lateinit var adapter: RepositoryRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initAdapter()
        initViews()
        bindViews()

    }

    private fun initAdapter() {
        adapter = RepositoryRecyclerAdapter()
    }

    private fun initViews() = with(binding) {
        emptyResultTextView.isGone = true
        recyclerView.adapter = adapter
    }

    private fun bindViews() = with(binding) {
        searchButton.setOnClickListener {
            searchKeyword(searchBarInputView.text.toString())
        }
    }
    private fun searchKeyword(keywordString: String) = launch {
        withContext(Dispatchers.IO) {
            val response = RetrofitUtil.githubApiService.searchRepositories(keywordString)
            if (response.isSuccessful) {
                val body = response.body()
                withContext(Dispatchers.Main) {
                    Log.e("response", body.toString())
                    body?.let {
                        setData(body.items)
                    }

                }
            }
        }
    }

    private fun setData(items: List<GithubRepoEntity>) {
        adapter.setRepositoryList(items) {
            Toast.makeText(this, "entity: $it", Toast.LENGTH_SHORT).show()
        }

    }

}