package com.doyeon.chapter10.searchaddress

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.doyeon.chapter10.searchaddress.databinding.ActivityMapBinding
import com.doyeon.chapter10.searchaddress.model.LocationLatLngEntity
import com.doyeon.chapter10.searchaddress.model.SearchResultEntity
import com.doyeon.chapter10.searchaddress.utility.RetrofitUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.*
import retrofit2.Retrofit
import kotlin.coroutines.CoroutineContext

class MapActivity: AppCompatActivity(), OnMapReadyCallback, CoroutineScope  {
    private val TAG = "MapActivity"
    private lateinit var googleMap: GoogleMap
    private lateinit var searchResult: SearchResultEntity
    private var currentSelectMarker: Marker? = null
    private lateinit var locationManager: LocationManager
    private lateinit var myLocationListener: MyLocationListener

    private lateinit var binding: ActivityMapBinding

    private lateinit var job: Job

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    companion object {
        const val SEARCH_RESULT_EXTRA_KEY: String = "SEARCH_RESULT_EXTRA_KEY"
        const val CAMERA_ZOOM_LEVEL = 17f
        const val PERMISSION_REQUEST_CODE = 101
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        job = Job()
        bindViews()
        setContentView(binding.root)


        if(::searchResult.isInitialized.not()) {
            //???????????? ????????????:
            //??????????????? ????????? ???????????? ??? ????????????(::) ??? ???????????? ????????? ?????? ????????? ????????? ??? ??????. ??????????????? ???????????? ????????? ?????? ????????? ????????? ??? ??????????????????.
            intent?.let {
                searchResult = it.getParcelableExtra<SearchResultEntity>(SEARCH_RESULT_EXTRA_KEY) ?: throw Exception("???????????? ????????????.")
                setupGoogleMap()
            }
        }

    }

    private fun bindViews() = with(binding){
        currentLocationButton.setOnClickListener{
            getMyLocation()
        }
    }

    private fun setupGoogleMap() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(map: GoogleMap) {
        this.googleMap = map
        currentSelectMarker = setupMarker(searchResult)
        currentSelectMarker?.showInfoWindow()

    }

    private fun setupMarker(searchResult: SearchResultEntity): Marker? {
        val positionLatLng = LatLng(
            searchResult.locationLatLng.latitude.toDouble(),
            searchResult.locationLatLng.longitude.toDouble()
        )

        val markerOptions = MarkerOptions().apply {
            position(positionLatLng)
            title(searchResult.name)
            snippet(searchResult.fullAddress)

        }

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(positionLatLng, CAMERA_ZOOM_LEVEL))
        return googleMap.addMarker(markerOptions)

    }

    private fun getMyLocation() {
        Log.d(TAG, "MapActivity - getMyLocation() called")
        if (::locationManager.isInitialized.not()) {
            Log.d(TAG, "MapActivity - getMyLocation() called")
            locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }

        val isGpsEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (isGpsEnable) {
            if(ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                    ,
                    PERMISSION_REQUEST_CODE
                )
            } else {
                setMyLocationListener()
            }
        }
    }
    @SuppressLint("MissingPermission")
    private fun setMyLocationListener() {
        val minTime = 1500L
        val minDistance = 100f

        if (::myLocationListener.isInitialized.not()) {
            myLocationListener = MyLocationListener()
        }

        with(locationManager) {
            requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                minTime,
                minDistance,
                myLocationListener
            )

            requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                minTime,
                minDistance,
                myLocationListener
            )
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if( requestCode == PERMISSION_REQUEST_CODE) {
            if ( grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                setMyLocationListener()
            } else {
                Toast.makeText(this, "????????? ?????? ???????????????", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onCurrentLocationChanged(locationLatLngEntity: LocationLatLngEntity) {
        val positionLatLng = LatLng(
            locationLatLngEntity.latitude.toDouble(),
            locationLatLngEntity.longitude.toDouble()
        )
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(positionLatLng, CAMERA_ZOOM_LEVEL))
        loadReverseGeoInformation(locationLatLngEntity)
        removeLocationListener()
    }

    private fun loadReverseGeoInformation(locationLatLngEntity: LocationLatLngEntity) {
        launch(coroutineContext) {
            try {
                withContext(Dispatchers.IO) {
                    //network
                    val response = RetrofitUtil.apiService.getReverseGeoCode(
                        lat = locationLatLngEntity.latitude.toDouble(),
                        lon = locationLatLngEntity.longitude.toDouble()
                    )

                    if (response.isSuccessful) {
                        withContext(Dispatchers.Main) {
                            val body = response.body()
                            Log.d(TAG, "loadReverseGeoInformation: ${body.toString()}")
                            body?.let {
                                currentSelectMarker = setupMarker(
                                    SearchResultEntity(
                                        fullAddress = body.addressInfo.fullAddress ?: "????????????",
                                        name = "?????????",
                                        locationLatLng = locationLatLngEntity
                                    )
                                )
                                currentSelectMarker?.showInfoWindow()
                            }
                        }
                    }
                }



            }catch (e: Exception) {
                e.printStackTrace()
                Log.d(TAG, "MapActivity - loadReverseGeoInformation() ???????????? ???????????? ????????? ??????????????????")
                Toast.makeText(this@MapActivity, "????????? ?????? ???????????????", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun removeLocationListener() {
        if(::locationManager.isInitialized  && ::myLocationListener.isInitialized) {
            locationManager.removeUpdates(myLocationListener)
        }
    }

    inner class MyLocationListener: LocationListener {
        override fun onLocationChanged(location: Location) {
            val locationLatLngEntity = LocationLatLngEntity(
                location.latitude.toFloat(),
                location.longitude.toFloat()
            )
            onCurrentLocationChanged(locationLatLngEntity)
        }

    }
}