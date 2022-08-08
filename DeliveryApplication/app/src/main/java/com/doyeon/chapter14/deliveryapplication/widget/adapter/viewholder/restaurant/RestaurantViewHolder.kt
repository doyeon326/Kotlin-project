package com.doyeon.chapter14.deliveryapplication.widget.adapter.viewholder.restaurant

import com.doyeon.chapter14.deliveryapplication.databinding.ViewholderEmptyBinding
import com.doyeon.chapter14.deliveryapplication.model.Model
import com.doyeon.chapter14.deliveryapplication.model.restaurant.RestaurantModel
import com.doyeon.chapter14.deliveryapplication.screen.base.BaseViewModel
import com.doyeon.chapter14.deliveryapplication.util.provider.DefaultResourcesProvider
import com.doyeon.chapter14.deliveryapplication.util.provider.ResourcesProvider
import com.doyeon.chapter14.deliveryapplication.widget.adapter.listener.AdapterListener
import com.doyeon.chapter14.deliveryapplication.widget.adapter.listener.RestaurantListListener
import com.doyeon.chapter14.deliveryapplication.widget.adapter.viewholder.ModelViewHolder

class RestaurantViewHolder(
    private val binding: ViewholderEmptyBinding,
    viewModel: BaseViewModel,
    resourcesProvider: ResourcesProvider
): ModelViewHolder<RestaurantModel>(binding, viewModel, resourcesProvider) {

    override fun reset()  = Unit


    override fun bindData(model: RestaurantModel) {
        super.bindData(model)
    }
    override fun bindViews(model: RestaurantModel, adapterListener: AdapterListener) = with(binding) {
        if (adapterListener is RestaurantListListener ) {
            adapterListener.oncClickItem(model)
        }
    }
}