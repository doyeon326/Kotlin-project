package com.doyeon.chapter14.deliveryapplication.screen.mylocation

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.doyeon.chapter14.deliveryapplication.R
import com.doyeon.chapter14.deliveryapplication.data.entity.LocationLatLngEntity
import com.doyeon.chapter14.deliveryapplication.data.entity.MapSearchInfoEntity
import com.doyeon.chapter14.deliveryapplication.databinding.ActivityMyLocationBinding
import com.doyeon.chapter14.deliveryapplication.screen.base.BaseActivity
import com.doyeon.chapter14.deliveryapplication.screen.main.home.HomeViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MyLocationActivity : BaseActivity<MyLocationViewModel, ActivityMyLocationBinding>(),
    OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private var isMapInitialized: Boolean = false
    private var isChangeLocation: Boolean = false

    companion object {
        fun newIntent(context: Context, mapSearchInfoEntity: MapSearchInfoEntity) =
            Intent(context, MyLocationActivity::class.java).apply {
                putExtra(HomeViewModel.MY_LOCATION_KEY, mapSearchInfoEntity)
            }
        const val CAMERA_ZOOM_LEVEL = 17f
    }

    override fun onMapReady(map: GoogleMap) {
        this.map = map ?: return
        viewModel.fetchData()
    }
    override val viewModel by viewModel<MyLocationViewModel> {
        parametersOf(
            intent.getParcelableExtra<MapSearchInfoEntity>(HomeViewModel.MY_LOCATION_KEY)
        )
    }
    override fun initViews() = with(binding) {
        toolbar.setNavigationOnClickListener {
            finish()
        }
        confirmButton.setOnClickListener {
            viewModel.confirmSelectLocation()
        }
        setupGoogleMap()
    }

    override fun getViewBinding(): ActivityMyLocationBinding = ActivityMyLocationBinding.inflate(layoutInflater)

    override fun observeData() = viewModel.myLocationStateLiveData.observe(this) {
        when (it) {
            is MyLocationState.Loading -> {
                handleLoadingState()
            }

            is MyLocationState.Success -> {
                if(::map.isInitialized) {
                    handleSuccessState(it)
                }
            }

            is MyLocationState.Confirm -> {
                setResult(Activity.RESULT_OK, Intent().apply {
                    putExtra(HomeViewModel.MY_LOCATION_KEY, it.mapSearchInfoEntity)
                })
                finish()

            } is MyLocationState.Error -> {
            Toast.makeText(this, it.messageId, Toast.LENGTH_SHORT).show()
            } else -> Unit
        }
    }


    private fun setupGoogleMap(){
        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun handleLoadingState() = with(binding) {
        locationLoading.isVisible = true
        locationTitleText.text = getString(R.string.loading)
    }

    private fun handleSuccessState(state: MyLocationState.Success) = with(binding) {
        val mapSearchinfo = state.mapSearchInfoEntity
        locationLoading.isGone = true
        locationTitleText.text = mapSearchinfo.fullAddress

        if (isMapInitialized.not()) {
            map.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        mapSearchinfo.locationLatLng.latitude,
                        mapSearchinfo.locationLatLng.longitude
                    ), CAMERA_ZOOM_LEVEL
                )
            )
            map.setOnCameraIdleListener {
                if (isChangeLocation.not()) {
                    isChangeLocation = true
                    Handler(Looper.getMainLooper()).postDelayed({
                        val cameraLatLng = map.cameraPosition.target
                        viewModel.changeLocationInfo(
                            LocationLatLngEntity(
                                cameraLatLng.latitude,
                                cameraLatLng.longitude
                            )
                        )
                        isChangeLocation = false
                    },1000)
                }

            }
            isMapInitialized = true
        }
    }




}