package com.doyeon.chapter10.searchaddress

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.doyeon.chapter10.searchaddress.databinding.ActivityMapBinding
import com.doyeon.chapter10.searchaddress.model.SearchResultEntity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapActivity: AppCompatActivity(), OnMapReadyCallback  {

    private lateinit var googleMap: GoogleMap
    private lateinit var searchResult: SearchResultEntity
    private var currentSelectMarker: Marker? = null

    private lateinit var binding: ActivityMapBinding
    companion object {
        val SEARCH_RESULT_EXTRA_KEY: String = "SEARCH_RESULT_EXTRA_KEY"
        const val CAMERA_ZOOM_LEVEL = 17f
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(::searchResult.isInitialized.not()) {
            //더블클론 사용이유:
            //코틀린에서 변수나 클래스명 앞 더블클론(::) 을 명시하면 변수에 대한 속성을 참고할 수 있다. 더블콜론을 명시하면 변수가 아닌 객체로 인식할 수 있기때문이다.
            intent?.let {
                searchResult = it.getParcelableExtra<SearchResultEntity>(SEARCH_RESULT_EXTRA_KEY) ?: throw Exception("데이터가 없습니다.")
                setupGoogleMap()
            }
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
}