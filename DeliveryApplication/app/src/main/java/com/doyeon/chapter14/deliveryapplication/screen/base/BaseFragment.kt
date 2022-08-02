package com.doyeon.chapter14.deliveryapplication.screen.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Job

abstract class BaseFragment<VM: BaseViewModel, VB: ViewBinding> : Fragment() {
    abstract val viewModel: VM
    protected lateinit var binding: VB
    protected lateinit var fetchJob: Job

    abstract fun getViewBinding(): VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = getViewBinding()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initState()
    }


    open fun initState() {
        //상태 초기
        arguments?.let {
            viewModel.storeState(it)
        }
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