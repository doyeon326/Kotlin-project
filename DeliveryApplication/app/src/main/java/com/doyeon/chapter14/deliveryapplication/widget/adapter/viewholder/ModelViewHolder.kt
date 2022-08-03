package com.doyeon.chapter14.deliveryapplication.widget.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.doyeon.chapter14.deliveryapplication.model.Model
import com.doyeon.chapter14.deliveryapplication.screen.base.BaseViewModel
import com.doyeon.chapter14.deliveryapplication.util.provider.ResourcesProvider
import com.doyeon.chapter14.deliveryapplication.widget.adapter.listener.AdapterListener

abstract class ModelViewHolder<M: Model>(
    binding: ViewBinding,
    protected val viewModel: BaseViewModel,
    protected val resourcesProvider: ResourcesProvider
) : RecyclerView.ViewHolder(binding.root){

    abstract fun reset()

    open fun bindData(model: M) {
        reset()
    }

    abstract fun bindViews(model: M, adapterListener: AdapterListener)


}