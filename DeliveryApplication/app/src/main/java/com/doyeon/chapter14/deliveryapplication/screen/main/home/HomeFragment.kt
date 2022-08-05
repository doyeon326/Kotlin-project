package com.doyeon.chapter14.deliveryapplication.screen.main.home

import android.util.Log
import com.doyeon.chapter14.deliveryapplication.databinding.FragmentHomeBinding
import com.doyeon.chapter14.deliveryapplication.screen.base.BaseFragment
import com.doyeon.chapter14.deliveryapplication.screen.main.home.restraurant.RestaurantCategory
import com.doyeon.chapter14.deliveryapplication.screen.main.home.restraurant.RestaurantListFragment
import com.doyeon.chapter14.deliveryapplication.widget.adapter.RestaurantListFragmentPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment: BaseFragment<HomeViewModel, FragmentHomeBinding>() {

    override val viewModel by viewModel<HomeViewModel>()
    override fun getViewBinding(): FragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)

    private lateinit var viewPagerAdapter: RestaurantListFragmentPagerAdapter


    override fun initViews() {
        super.initViews()
        initViewPager()
    }

    private fun initViewPager() = with(binding) {

        val restaurantCategories = RestaurantCategory.values()
        Log.d("initViewPager()", "${restaurantCategories.size}")
        if(::viewPagerAdapter.isInitialized.not()) {

            val restaurantListFragmentList = restaurantCategories.map {
                RestaurantListFragment.newInstance(it)
            }

            viewPagerAdapter = RestaurantListFragmentPagerAdapter(
                this@HomeFragment,
                restaurantListFragmentList
            )
            viewPager.adapter = viewPagerAdapter
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->

                tab.setText(restaurantCategories[position].categoryNameId)
            }.attach()

        }



    }



    override fun observeData() {

    }
    companion object {
        fun newInstance() = HomeFragment()
        const val TAG = "HomeFragment"
    }
}