package com.doyeon.chapter8.myapplication

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.doyeon.chapter8.myapplication.adapter.VideoAdapter
import com.doyeon.chapter8.myapplication.databinding.FragmentPlayerBinding
import com.doyeon.chapter8.myapplication.dto.VideoDto
import com.doyeon.chapter8.myapplication.service.VideoService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.abs

class PlayerFragment: Fragment(R.layout.fragment_player) {

    private lateinit var videoAdapter: VideoAdapter
    private var binding: FragmentPlayerBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentPlayerBinding = FragmentPlayerBinding.bind(view)
        binding = fragmentPlayerBinding

        initMotionLayoutEvent(fragmentPlayerBinding)
        initRecyclerView(fragmentPlayerBinding)
        getVideoList()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun initMotionLayoutEvent(fragmentPlayerBinding: FragmentPlayerBinding) {
        fragmentPlayerBinding.playerMotionLayout.setTransitionListener(object: MotionLayout.TransitionListener {
            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int
            ) {

            }

            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float
            ) {
                binding?.let {
                    (activity as MainActivity).also { mainActivity ->
                        mainActivity.findViewById<MotionLayout>(R.id.mainMotionLayout).progress = abs(progress)
                    }
                }
            }

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {

            }

            override fun onTransitionTrigger(
                motionLayout: MotionLayout?,
                triggerId: Int,
                positive: Boolean,
                progress: Float
            ) {

            }

        })
    }

    private fun initRecyclerView(fragmentPlayerBinding: FragmentPlayerBinding) {
        videoAdapter = VideoAdapter(callback = { url, title ->
        //todo
            play(url, title)
        })
        fragmentPlayerBinding.fragmentRecyclerView.apply {
            //초기화

            adapter = videoAdapter
            layoutManager = LinearLayoutManager(context)
        }
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

    fun play(url: String, title: String) {
        binding?.let {
            it.playerMotionLayout.transitionToEnd()
            it.bottomTitleTextView.text = title
            
        }
    }
}