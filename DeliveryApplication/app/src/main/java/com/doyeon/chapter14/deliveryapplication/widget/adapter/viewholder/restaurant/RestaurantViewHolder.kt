package com.doyeon.chapter14.deliveryapplication.widget.adapter.viewholder.restaurant

import com.doyeon.chapter14.deliveryapplication.R
import com.doyeon.chapter14.deliveryapplication.databinding.ViewholderEmptyBinding
import com.doyeon.chapter14.deliveryapplication.databinding.ViewholderRestaurantBinding
import com.doyeon.chapter14.deliveryapplication.extensions.clear
import com.doyeon.chapter14.deliveryapplication.extensions.load
import com.doyeon.chapter14.deliveryapplication.model.Model
import com.doyeon.chapter14.deliveryapplication.model.restaurant.RestaurantModel
import com.doyeon.chapter14.deliveryapplication.screen.base.BaseViewModel
import com.doyeon.chapter14.deliveryapplication.util.provider.DefaultResourcesProvider
import com.doyeon.chapter14.deliveryapplication.util.provider.ResourcesProvider
import com.doyeon.chapter14.deliveryapplication.widget.adapter.listener.AdapterListener
import com.doyeon.chapter14.deliveryapplication.widget.adapter.listener.RestaurantListListener
import com.doyeon.chapter14.deliveryapplication.widget.adapter.viewholder.ModelViewHolder

class RestaurantViewHolder(
    private val binding: ViewholderRestaurantBinding,
    viewModel: BaseViewModel,
    resourcesProvider: ResourcesProvider
) : ModelViewHolder<RestaurantModel>(binding, viewModel, resourcesProvider) {

    override fun reset() = with(binding) {
        restaurantImage.clear()
    }


    override fun bindData(model: RestaurantModel) {
        super.bindData(model)

        with(binding) {
            restaurantImage.load(model.restaurantImageUrl, 24f)
            restaurantTitleText.text = model.restaurantTitle
            gradeText.text = resourcesProvider.getString(R.string.grade_format, model.grade)
            reviewCountText.text =
                resourcesProvider.getString(R.string.review_count, model.reviewCount)
            val (minTip, maxTip) = model.deliveryTimeRange
            deliveryTipText.text =
                resourcesProvider.getString(R.string.low_delivery_tip, minTip, maxTip)

        }
    }

    override fun bindViews(model: RestaurantModel, adapterListener: AdapterListener) =
        with(binding) {
            if (adapterListener is RestaurantListListener) {
                root.setOnClickListener {
                    adapterListener.oncClickItem(model)
                }
            }
        }
}