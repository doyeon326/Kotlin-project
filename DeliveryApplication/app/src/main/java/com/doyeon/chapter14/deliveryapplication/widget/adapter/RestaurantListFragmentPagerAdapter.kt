package com.doyeon.chapter14.deliveryapplication.widget.adapter

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.doyeon.chapter14.deliveryapplication.screen.main.home.restraurant.RestaurantListFragment

class RestaurantListFragmentPagerAdapter(
    fragment: Fragment,
    private val fragmentList: List<RestaurantListFragment>
    ): FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment = fragmentList[position]
}