package com.doyeon.chapter14.deliveryapplication.screen.main.home.restaurant.detail

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import com.doyeon.chapter14.deliveryapplication.R
import com.doyeon.chapter14.deliveryapplication.data.entity.RestaurantEntity
import com.doyeon.chapter14.deliveryapplication.databinding.ActivityRestaurantDetailBinding
import com.doyeon.chapter14.deliveryapplication.extensions.fromDpToPx
import com.doyeon.chapter14.deliveryapplication.extensions.load
import com.doyeon.chapter14.deliveryapplication.screen.base.BaseActivity
import com.doyeon.chapter14.deliveryapplication.screen.main.home.restaurant.RestaurantListFragment
import com.google.android.material.appbar.AppBarLayout
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import kotlin.math.abs

class RestaurantDetailActivity :BaseActivity<RestaurantDetailViewModel, ActivityRestaurantDetailBinding>() {

    companion object {
        fun newIntent(context: Context, restaurantEntity: RestaurantEntity) = Intent(context, RestaurantDetailActivity::class.java).apply {
            putExtra(RestaurantListFragment.RESTAURANT_KEY, restaurantEntity)
        }

    }

    override fun getViewBinding(): ActivityRestaurantDetailBinding = ActivityRestaurantDetailBinding.inflate(layoutInflater)
    override val viewModel by viewModel<RestaurantDetailViewModel> {
        parametersOf(
            intent.getParcelableExtra<RestaurantEntity>(RestaurantListFragment.RESTAURANT_KEY)
        )
    }



    override fun initViews() {
        initAppBar()
    }

    private fun initAppBar() = with(binding) {
        appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val topPadding = 300f.fromDpToPx().toFloat()
            val abstractOffset = abs(verticalOffset)
            val realAlphaScrollHeight = appBarLayout.measuredHeight - appBarLayout.totalScrollRange
            val realAlphaVerticalOffset: Float = if ( abstractOffset - topPadding < 0 ) 0f else abstractOffset - topPadding

            if (abstractOffset < topPadding) {
                restaurantTitleTextView.alpha = 0f
                return@OnOffsetChangedListener
            }
            val percentage = realAlphaVerticalOffset / realAlphaScrollHeight
            restaurantTitleTextView.alpha = 1 - (if (1 - percentage * 2 < 0) 0f else 1 - percentage * 2)
        })

        toolbar.setNavigationOnClickListener { finish() }
        callButton.setOnClickListener {  }
        likeButton.setOnClickListener {  }
        shareButton.setOnClickListener {  }
    }

    override fun observeData() = viewModel.restaurantDetailLiveData.observe(this){
        when (it) {
            is RestaurantDetailState.Success -> {
                handleSuccess(it)
            }
        }
    }

    private fun handleSuccess(state: RestaurantDetailState.Success) = with(binding) {
        val restaurantEntity = state.restaurantEntity


        callButton.isGone = restaurantEntity.restaurantTelNum == null
        restaurantTitleTextView.text = restaurantEntity.restaurantTitle
        restaurantImage.load(restaurantEntity.restaurantImageUrl)
        restaurantMainTitleTextView.text = restaurantEntity.restaurantTitle
        ratingBar.rating = restaurantEntity.grade
        deliveryTimeText.text = getString(
            R.string.delivery_expected_time,
            restaurantEntity.deliveryTimeRange.first,
            restaurantEntity.deliveryTimeRange.second
        )
        deliveryTipText.text = getString(
            R.string.delivery_tip_range,
            restaurantEntity.deliveryTipRange.first,
            restaurantEntity.deliveryTipRange.second
        )

        likeText.setCompoundDrawablesWithIntrinsicBounds(
            ContextCompat.getDrawable(
                this@RestaurantDetailActivity, if (state.isLiked == true) {
                    R.drawable.ic_heart_enable
                } else {
                    R.drawable.ic_heart_disable
                }
            ), null, null, null
        )
    }
}