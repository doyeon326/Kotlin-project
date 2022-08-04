package com.doyeon.chapter14.deliveryapplication.screen.main.home

import com.doyeon.chapter14.deliveryapplication.databinding.FragmentHomeBinding
import com.doyeon.chapter14.deliveryapplication.databinding.FragmentMyBinding
import com.doyeon.chapter14.deliveryapplication.screen.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyFragment: BaseFragment<MyViewModel, FragmentMyBinding>() {

    override val viewModel by viewModel<MyViewModel>()
    override fun getViewBinding(): FragmentMyBinding = FragmentMyBinding.inflate(layoutInflater)
    override fun observeData() {

    }

    companion object {
        fun newInstance() = MyFragment()
        const val TAG = "MyFragment"
    }



}