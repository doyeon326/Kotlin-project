package com.doyeon.chapter8.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.doyeon.chapter8.myapplication.adapter.VideoAdapter
import com.doyeon.chapter8.myapplication.dto.VideoDto
import com.doyeon.chapter8.myapplication.service.VideoService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var videoAdapter: VideoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, PlayerFragment())
            .commit()

        videoAdapter = VideoAdapter(callback = { url, title ->
            supportFragmentManager.fragments.find { it is PlayerFragment }?.let {
                (it as PlayerFragment).play(url,title)
            }

        })

        findViewById<RecyclerView>(R.id.mainRecyclerView).apply {
            adapter = videoAdapter
            layoutManager = LinearLayoutManager(context)
        }

        getVideoList()
    }

    private fun getVideoList() {
        Log.d("MainActivity", "getVideoList")
        val retrofit = Retrofit.Builder()
            .baseUrl("https://run.mocky.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(VideoService::class.java).also {
            it.listVideo()
                .enqueue(object: Callback<VideoDto> {
                    override fun onResponse(call: Call<VideoDto>, response: Response<VideoDto>) {
                        if (response.isSuccessful.not()) {
                            Log.d("MainActivity", "response Fail")
                            return
                        }
                        response.body()?.let { videoDto ->
                            Log.d("MainActivity", "${it.toString()}")
                            videoAdapter.submitList(videoDto.videos)
                        }


                    }

                    override fun onFailure(call: Call<VideoDto>, t: Throwable) {
                        Log.d("MainActivity", "response Fail ${t.toString()}")
                    }

                })
        }
    }
}