package com.doyeon.chapter10.searchaddress

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import com.doyeon.chapter10.searchaddress.databinding.ActivityMainBinding
import com.doyeon.chapter10.searchaddress.model.LocationLatLngEntity
import com.doyeon.chapter10.searchaddress.model.SearchResultEntity
import com.doyeon.chapter10.searchaddress.response.search.Poi
import com.doyeon.chapter10.searchaddress.response.search.Pois
import com.doyeon.chapter10.searchaddress.response.search.SearchResponse
import com.doyeon.chapter10.searchaddress.utility.RetrofitUtil
import kotlinx.coroutines.*
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {

    val TAG: String = "로그"
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: SearchRecyclerAdapter
    private lateinit var job: Job

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        job = Job()

        val view = binding.root
        setContentView(view)
        initAdapter()
        initViews()
        bindViews()
        initData()


    }

    private fun bindViews() = with(binding) {
        searchButton.setOnClickListener {
            Log.d(TAG, "MainActivity - bindViews() called ${searchBarInputView.text.toString()}")
            searchKeyword(searchBarInputView.text.toString())
        }
    }

    private fun initViews() = with(binding){
        //뷰 초기화
        emptyResultTextView.isVisible = false
        recyclerView.adapter = adapter
    }

    private fun initAdapter() {
        adapter = SearchRecyclerAdapter()
    }

    private fun initData() {
        adapter.notifyDataSetChanged()
    }

    private fun setData(pois: Pois) {
        val dataList = pois.poi.map {
            SearchResultEntity(
                name = it.name ?: "빌딩명 없음",
                fullAddress = makeMainAdress(it),
                locationLatLng = LocationLatLngEntity(
                    it.noorLat,
                    it.noorLon
                )
            )
        }
        adapter.setSearchResultList(dataList) {
            Toast.makeText(this, "빌딩이름: ${it.name}, 주소: ${it.fullAddress} 위도: ${it.locationLatLng}", Toast.LENGTH_SHORT).show()
            startActivity(
                Intent(this, MapActivity::class.java)
            )

        }

    }

    private fun searchKeyword(keywordString: String) {
        launch(coroutineContext) {
            try {
                withContext(Dispatchers.IO) {
                    val response = RetrofitUtil.apiService.getSearchLocation(
                        keyword = keywordString
                    )
                    if (response.isSuccessful) {
                        val body = response.body()
                        withContext(Dispatchers.Main){
                            Log.d("MainActivity", body.toString())

                            body?.let { searchResponse ->  
                                setData(searchResponse.searchPoiInfo.pois)
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Log.d("MainActivity", "${e}")
               e
            }
        }
    }

    private fun makeMainAdress(poi: Poi): String =
        if (poi.secondNo?.trim().isNullOrEmpty()) {
            (poi.upperAddrName?.trim() ?: "") + " " +
                    (poi.middleAddrName?.trim() ?: "") + " " +
                    (poi.lowerAddrName?.trim() ?: "") + " " +
                    (poi.detailAddrName?.trim() ?: "") + " " +
                    poi.firstNo?.trim()
        } else {
            (poi.upperAddrName?.trim() ?: "") + " " +
                    (poi.middleAddrName?.trim() ?: "") + " " +
                    (poi.lowerAddrName?.trim() ?: "") + " " +
                    (poi.detailAddrName?.trim() ?: "") + " " +
                    (poi.firstNo?.trim() ?: "") + " " +
                    poi.secondNo?.trim()
        }




}