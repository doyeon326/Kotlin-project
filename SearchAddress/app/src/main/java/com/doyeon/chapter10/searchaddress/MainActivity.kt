package com.doyeon.chapter10.searchaddress

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import com.doyeon.chapter10.searchaddress.databinding.ActivityMainBinding

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

    }

    private fun initViews() = with(binding){
        //뷰 초기화
        emptyResultTextView.isVisible = false
        recyclerView.adapter = adapter



    }

    private fun initAdapter() {
        adapter = SearchRecyclerAdapter{
            Toast.makeText(this,"클릭이 되었습니다", Toast.LENGTH_SHORT).show()
        }
    }
}