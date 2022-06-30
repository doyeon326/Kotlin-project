package com.doyeon.chapter4.myapplication

import com.doyeon.chapter4.myapplication.adapter.BookAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.doyeon.chapter4.myapplication.adapter.HistoryAdapter
import com.doyeon.chapter4.myapplication.api.BookService
import com.doyeon.chapter4.myapplication.databinding.ActivityMainBinding
import com.doyeon.chapter4.myapplication.model.BestSellerDto
import com.doyeon.chapter4.myapplication.model.History
import com.doyeon.chapter4.myapplication.model.SearchBookDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//FCBBD6E01A91729F1C5CE594910690406D9D338E2F5DC815BF53BD4AFA0D5A64

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: BookAdapter
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var bookService: BookService

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBookRecyclerView()
        initHistoryRecyclerView()
        //initSearchEditText()

        //create database
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "BookSearchDB"
        ).build()

        val retrofit =  Retrofit.Builder()
            .baseUrl("https://book.interpark.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        bookService = retrofit.create(BookService::class.java)
        bookService.getBestSellerBooks(getString(R.string.interparkAPIKey))
            .enqueue(object: Callback<BestSellerDto>{
                override fun onResponse(
                    call: Call<BestSellerDto>,
                    response: Response<BestSellerDto>
                ) {
                    if (response.isSuccessful.not()) {
                        Log.d(TAG,  "NOT SUCCESSFUL ${response.message()}")
                        return
                    }
                    response.body()?.let {
                        Log.d(TAG, it.toString())
                        it.books.forEach { book ->
                            Log.d(TAG, book.toString())
                        }

                        adapter.submitList(it.books)
                    }
                }

                override fun onFailure(call: Call<BestSellerDto>, t: Throwable) {
                    Log.e(TAG, t.toString())
                }
            })

    }

    private fun search(keyword: String) {
        bookService.getBooksByName(getString(R.string.interparkAPIKey), keyword)
            .enqueue(object : Callback<SearchBookDto> {
                override fun onResponse( call: Call<SearchBookDto>, response: Response<SearchBookDto>) {
                    hideHistoryView()
                    saveSearchKeyword(keyword)
                    if (response.isSuccessful.not()) {
                        Log.d(TAG,  "NOT SUCCESSFUL")
                        return
                    }
                    adapter.submitList(response.body()?.books.orEmpty())

                }

                override fun onFailure(call: Call<SearchBookDto>, t: Throwable) {
                    hideHistoryView()
                    Log.e(TAG, t.toString())
                }
            })
    }

    private fun initBookRecyclerView() {
        adapter = BookAdapter()
        binding.bookRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.bookRecyclerView.adapter = adapter
    }

    private fun initHistoryRecyclerView() {
        historyAdapter = HistoryAdapter(historyDeleteClickedLister = {
            Log.d(TAG, "message: ${it}")
            deleteSearchKeyword(it)
        })

        binding.historyRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.historyRecyclerView.adapter = historyAdapter
        initSearchEditText()

    }

    private fun saveSearchKeyword(keyword: String) {
        Thread {
            db.historyDao().insertHistory(History(null, keyword))
        }.start()
    }

    private fun deleteSearchKeyword(keyword: String) {
        Thread {
            db.historyDao().delete(keyword)
            showHistoryView()
        }.start()
    }

    private fun showHistoryView() {
        Log.d(TAG, "showHistoryView fun called")
        Thread {
            val keywords = db.historyDao().getAll().reversed()
            Log.d(TAG, "keywords: ${keywords}" )

            runOnUiThread {
                binding.historyRecyclerView.isVisible = true
                historyAdapter.submitList(keywords.orEmpty())
            }
        }.start()

        binding.historyRecyclerView.isVisible = true
    }

    private fun hideHistoryView() {
        binding.historyRecyclerView.isVisible = false
    }

    private fun initSearchEditText() {
        binding.searchEditText.setOnKeyListener { view, keyCode, event ->


            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == MotionEvent.ACTION_DOWN) {
                //2번의 키코드 이벤트가 들어온다: 업 & 다운
                Log.d(TAG, "key code: ${keyCode}")
                search(binding.searchEditText.text.toString())
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

        binding.searchEditText.setOnTouchListener { view, motionEvent ->
            Log.d(TAG, "motionevent = ${MotionEvent.ACTION_DOWN}")
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                Log.d(TAG, "motionevent = true")

                showHistoryView()
            }
            return@setOnTouchListener false
        }
    }
    companion object {
        private const val TAG = "MainActivity"
    }
}