package com.doyeon.chapter14.deliveryapplication.screen.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Job

abstract class BaseActivity<VM: BaseViewModel, VB: ViewBinding> : AppCompatActivity() {
    abstract val viewModel: VM
    protected lateinit var binding: VB
    protected lateinit var fetchJob: Job

    abstract fun getViewBinding(): VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewBinding()
        setContentView(binding.root)
        initState()
    }

    open fun initState() {
        //상태 초기
        initViews()
        fetchJob = viewModel.fetchData()
        observeData()
    }
    open fun initViews() = Unit //뷰초기화
    abstract fun observeData() //데이터 관찰

    override fun onDestroy() {
        if (fetchJob.isActive) { //active 할때 cancle 해줘야함
            fetchJob.cancel()
        }
        super.onDestroy()
    }

}