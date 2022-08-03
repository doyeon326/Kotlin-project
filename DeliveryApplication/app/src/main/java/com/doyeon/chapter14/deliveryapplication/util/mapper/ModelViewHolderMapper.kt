package com.doyeon.chapter14.deliveryapplication.util.mapper

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewParent
import com.doyeon.chapter14.deliveryapplication.databinding.ViewholderEmptyBinding
import com.doyeon.chapter14.deliveryapplication.model.CellType
import com.doyeon.chapter14.deliveryapplication.model.Model
import com.doyeon.chapter14.deliveryapplication.screen.base.BaseViewModel
import com.doyeon.chapter14.deliveryapplication.util.provider.ResourcesProvider
import com.doyeon.chapter14.deliveryapplication.widget.adapter.viewholder.EmptyViewHolder
import com.doyeon.chapter14.deliveryapplication.widget.adapter.viewholder.ModelViewHolder

object ModelViewHolderMapper {
    @Suppress("UNCHECKED_CAST")
    fun <M: Model> map (
        parent: ViewGroup,
        type: CellType,
        viewModel: BaseViewModel,
        resourcesProvider: ResourcesProvider
    ): ModelViewHolder<M> {
        val inflater = LayoutInflater.from(parent.context)
        val viewHolder = when (type) {
            CellType.EMPTY_CELL -> EmptyViewHolder(
                ViewholderEmptyBinding.inflate(inflater, parent, false),
                viewModel,
                resourcesProvider
            )
        }
         return viewHolder as ModelViewHolder<M>
    }
}