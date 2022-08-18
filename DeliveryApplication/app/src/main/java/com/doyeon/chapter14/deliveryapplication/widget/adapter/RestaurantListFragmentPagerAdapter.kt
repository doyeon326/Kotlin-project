package com.doyeon.chapter14.deliveryapplication.widget.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.doyeon.chapter14.deliveryapplication.data.entity.LocationLatLngEntity
import com.doyeon.chapter14.deliveryapplication.screen.main.home.restaurant.RestaurantListFragment

class RestaurantListFragmentPagerAdapter(
    fragment: Fragment,
    val fragmentList: List<RestaurantListFragment>,
    var locationLatLngEntity: LocationLatLngEntity
    ): FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment = fragmentList[position]
}