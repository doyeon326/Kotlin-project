package com.doyeon.chapter4.myapplication.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.doyeon.chapter4.myapplication.databinding.ItemHistoryBinding

import com.doyeon.chapter4.myapplication.model.History

class HistoryAdapter(val historyDeleteClickedLister: (String) -> Unit) : ListAdapter<History, HistoryAdapter.HistoryItemViewHolder>(diffUtil) {
    inner class HistoryItemViewHolder(private val binding: ItemHistoryBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(historyModel: History) {
            Log.d("HistoryAdapter", "bind: ${historyModel.keyword}")
            binding.historyKeywordTextView.text = historyModel.keyword

            binding.historyKeywordDeleteButton.setOnClickListener {
                historyDeleteClickedLister(historyModel.keyword.orEmpty())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryItemViewHolder {
        Log.d("HistoryAdapter", "")
        return HistoryItemViewHolder(ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: HistoryItemViewHolder, position: Int) {
        Log.d("HistoryAdapter", "current List: ${currentList[position]}")
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<History>() {
            override fun areContentsTheSame(oldItem: History, newItem: History) =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: History, newItem: History) =
                oldItem.keyword == newItem.keyword
        }
    }

}