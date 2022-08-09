package com.doyeon.chapter14.deliveryapplication.screen.main.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.doyeon.chapter14.deliveryapplication.R
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
    private lateinit var locationManager: LocationManager
    private lateinit var myLocationListener: MyLocationListener
    private var locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val responsePermission = permissions.entries.filter {
                (it.key == Manifest.permission.ACCESS_FINE_LOCATION)
                        || (it.key == Manifest.permission.ACCESS_COARSE_LOCATION)
            }
            if (responsePermission.filter { it.value }.size == locationPermission.size) {
                setMyLocationListener()
            } else {
                with (binding.locationTitleText) {
                    text = context.getString(R.string.please_setup_your_location_permission)
                    setOnClickListener {
                        getMyLocation()
                    }
                }
                Toast.makeText(requireContext(), getString(R.string.can_not_assigned_permission) , Toast.LENGTH_SHORT).show()
            }
        }

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



    override fun observeData() = viewModel.homeSateLiveData.observe(viewLifecycleOwner){
        when (it) {
            HomeState.Uninitialized -> {
                getMyLocation()
            }
        }
    }

    private fun getMyLocation() {
        if (::locationManager.isInitialized.not()) {
            locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }
        val isGpsUnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (isGpsUnabled) {
            locationPermissionLauncher.launch(locationPermission)
        }
    }

    @SuppressLint("MissingPermission")
    private fun setMyLocationListener() {
        val minTime = 1500L
        val minDistance = 100f

        if(::myLocationListener.isInitialized.not()) {
            myLocationListener = MyLocationListener()
        }
        with(locationManager) {
            requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                minTime, minDistance, myLocationListener
            )

            requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                minTime, minDistance, myLocationListener
            )
        }
    }
    companion object {

        val locationPermission = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        fun newInstance() = HomeFragment()
        const val TAG = "HomeFragment"
    }

    private fun removeLocationListener() {
        if (::myLocationListener.isInitialized && ::locationManager.isInitialized) {
            locationManager.removeUpdates(myLocationListener)
        }

    }

    inner class MyLocationListener: LocationListener {
        override fun onLocationChanged(location: Location) {
            binding.locationTitleText.text = "${location.latitude},${location.longitude}"
        }
    }
}