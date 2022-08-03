package com.doyeon.chapter14.deliveryapplication.widget.adapter.viewholder

import com.doyeon.chapter14.deliveryapplication.databinding.ViewholderEmptyBinding
import com.doyeon.chapter14.deliveryapplication.model.Model
import com.doyeon.chapter14.deliveryapplication.screen.base.BaseViewModel
import com.doyeon.chapter14.deliveryapplication.util.provider.DefaultResourcesProvider
import com.doyeon.chapter14.deliveryapplication.util.provider.ResourcesProvider
import com.doyeon.chapter14.deliveryapplication.widget.adapter.listener.AdapterListener

class EmptyViewHolder(
    private val binding: ViewholderEmptyBinding,
    viewModel: BaseViewModel,
    resourcesProvider: ResourcesProvider
): ModelViewHolder<Model>(binding, viewModel, resourcesProvider) {
    override fun reset()  = Unit
    override fun bindViews(model: Model, adapterListener: AdapterListener)  = Unit
}