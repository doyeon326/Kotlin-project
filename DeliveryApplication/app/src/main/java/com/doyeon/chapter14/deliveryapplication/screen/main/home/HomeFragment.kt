package com.doyeon.chapter14.deliveryapplication.screen.main.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.doyeon.chapter14.deliveryapplication.R
import com.doyeon.chapter14.deliveryapplication.data.entity.LocationLatLngEntity
import com.doyeon.chapter14.deliveryapplication.data.entity.MapSearchInfoEntity
import com.doyeon.chapter14.deliveryapplication.databinding.FragmentHomeBinding
import com.doyeon.chapter14.deliveryapplication.screen.base.BaseFragment
import com.doyeon.chapter14.deliveryapplication.screen.main.home.restraurant.RestaurantCategory
import com.doyeon.chapter14.deliveryapplication.screen.main.home.restraurant.RestaurantListFragment
import com.doyeon.chapter14.deliveryapplication.screen.mylocation.MyLocationActivity
import com.doyeon.chapter14.deliveryapplication.widget.adapter.RestaurantListFragmentPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment: BaseFragment<HomeViewModel, FragmentHomeBinding>() {

    override val viewModel by viewModel<HomeViewModel>()
    override fun getViewBinding(): FragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)

    private lateinit var viewPagerAdapter: RestaurantListFragmentPagerAdapter
    private lateinit var locationManager: LocationManager
    private lateinit var myLocationListener: MyLocationListener

    private val changeLocationLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if ( result.resultCode == Activity.RESULT_OK) {
                result.data?.getParcelableExtra<MapSearchInfoEntity>(HomeViewModel.MY_LOCATION_KEY)?.let { myLocationInfo ->
                    viewModel.loadReverseGeoInformation(myLocationInfo.locationLatLng)

                }
            }
        }

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

    override fun initViews() = with(binding){
        locationTitleText.setOnClickListener{
            viewModel.getMapSearchInfo()?.let { mapInfo ->
                changeLocationLauncher.launch(
                    MyLocationActivity.newIntent(
                        requireContext(), mapInfo
                    )
                )
            }
        }
    }


    private fun initViewPager(locationLatLng: LocationLatLngEntity) = with(binding) {
        val restaurantCategories = RestaurantCategory.values()
        if(::viewPagerAdapter.isInitialized.not()) {
            val restaurantListFragmentList = restaurantCategories.map { category ->
                RestaurantListFragment.newInstance(category, locationLatLng)
            }


            viewPagerAdapter = RestaurantListFragmentPagerAdapter(
                this@HomeFragment,
                restaurantListFragmentList,
                locationLatLng
            )
            viewPager.adapter = viewPagerAdapter
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.setText(restaurantCategories[position].categoryNameId)
            }.attach()
        }
        if (locationLatLng != viewPagerAdapter.locationLatLngEntity) {
            viewPagerAdapter.locationLatLngEntity = locationLatLng
            viewPagerAdapter.fragmentList.forEach {
                it.viewModel.setLocationLatLng(locationLatLng)
            }
        }
    }



    override fun observeData() = viewModel.homeSateLiveData.observe(viewLifecycleOwner){
        when (it) {

            HomeState.Uninitialized -> {
                getMyLocation()
            }
            HomeState.Loading -> {
                binding.locationLoading.isVisible = true
                binding.locationTitleText.text = getString(R.string.loading)
            }

            is HomeState.Success -> {
                binding.locationLoading.isGone = true
                binding.locationTitleText.text = it.mapSearchInfoEntity.fullAddress
                binding.tabLayout.isVisible = true
                binding.filterScrollView.isVisible = true
                binding.viewPager.isVisible = true
                binding.locationTitleText.setOnClickListener {
                    viewModel.getMapSearchInfo()?.let { mapInfo ->
                        changeLocationLauncher.launch(
                            MyLocationActivity.newIntent(
                                requireContext(), mapInfo
                            )
                        )
                    }
                }

                initViewPager(it.mapSearchInfoEntity.locationLatLng)
                if (it.isLocationSame.not()) {
                    Toast.makeText(requireContext(), R.string.please_setup_your_location_permission, Toast.LENGTH_SHORT).show()
                }

            }

            is HomeState.Error -> {
                binding.locationLoading.isGone = true
                binding.locationTitleText.setText(R.string.location_not_found)
                binding.locationTitleText.setOnClickListener {
                    getMyLocation()
                }
                Toast.makeText(requireContext(), it.messageId, Toast.LENGTH_SHORT).show()



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
           // binding.locationTitleText.text = ""
            Log.d(TAG, "onLocationChanged:${location.latitude},${location.longitude} ")
            viewModel.loadReverseGeoInformation(
                LocationLatLngEntity(
                    location.latitude,
                    location.longitude
                )
            )
            //todo remove location listenr
         //   removeLocationListener()
        }
    }
}