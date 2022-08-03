package com.doyeon.chapter14.deliveryapplication.model

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

abstract class Model (
    open val id: Long,
    open val type: CellType
        ) {
    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Model> = object : DiffUtil.ItemCallback<Model>() {
            //아이디가 같으면서 내용도같은지 확인할수있는 용도
            override fun areItemsTheSame(oldItem: Model, newItem: Model): Boolean {
              //아이디가 같은지 판단
                return oldItem.id == newItem.id && oldItem.type == newItem.type
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: Model, newItem: Model): Boolean {
            //같은 내용인지 판단
                return oldItem === newItem
            }
        }

    }
}