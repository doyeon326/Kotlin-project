package com.doyeon.chapter14.deliveryapplication.widget.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.doyeon.chapter14.deliveryapplication.model.CellType
import com.doyeon.chapter14.deliveryapplication.model.Model
import com.doyeon.chapter14.deliveryapplication.screen.base.BaseViewModel
import com.doyeon.chapter14.deliveryapplication.util.mapper.ModelViewHolderMapper
import com.doyeon.chapter14.deliveryapplication.util.provider.ResourcesProvider
import com.doyeon.chapter14.deliveryapplication.widget.adapter.listener.AdapterListener
import com.doyeon.chapter14.deliveryapplication.widget.adapter.viewholder.ModelViewHolder

class ModelRecyclerAdapter<M: Model, VM: BaseViewModel> (
    private var modelList: List<Model>,
    private val viewModel: VM,
    private val resourcesProvider: ResourcesProvider,
    private val adapterListener: AdapterListener


    ): ListAdapter<Model, ModelViewHolder<M>>(Model.DIFF_CALLBACK){

    override fun getItemCount(): Int = modelList.size

    override fun getItemViewType(position: Int): Int = modelList[position].type.ordinal

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModelViewHolder<M> {
        return ModelViewHolderMapper.map(parent, CellType.values()[viewType], viewModel, resourcesProvider)
    }

    override fun onBindViewHolder(holder: ModelViewHolder<M>, position: Int) {
         with(holder) {
            bindData( modelList[position] as M )
             bindViews(modelList[position] as M, adapterListener)
        }
    }

    override fun submitList(list: MutableList<Model>?) {
        list?.let { modelList = it }
        super.submitList(list)
    }

}