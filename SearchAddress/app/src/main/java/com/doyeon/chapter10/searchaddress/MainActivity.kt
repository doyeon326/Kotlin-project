package com.doyeon.chapter10.searchaddress

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import com.doyeon.chapter10.searchaddress.databinding.ActivityMainBinding
import com.doyeon.chapter10.searchaddress.model.LocationLatLngEntity
import com.doyeon.chapter10.searchaddress.model.SearchResultEntity

class MainActivity : AppCompatActivity() {

    val TAG: String = "로그"
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: SearchRecyclerAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)
        initAdapter()
        initViews()
        initData()
        setData()

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

    private fun setData() {
        val dataList = (0..10).map {
            SearchResultEntity(
                name = "빌딩 ${it}",
                fullAddress = "주소 ${it}",
                locationLatLng = LocationLatLngEntity(
                    latitude = it.toFloat(),
                    longitude = it.toFloat()
                )
            )
        }
        adapter.setSearchResultList(dataList) {
            Toast.makeText(this, "빌딩이름: ${it.name}, 주소: ${it.fullAddress}", Toast.LENGTH_SHORT).show()
        }

    }
}