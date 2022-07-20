package com.doyeon.chapter10.searchaddress

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.doyeon.chapter10.searchaddress.databinding.ViewholderSearchResultItemBinding

class SearchRecyclerAdapter(private val searchResultClickListener: (Any) -> Unit): RecyclerView.Adapter<SearchRecyclerAdapter.SearchResultItemViewHolder>() {

    private var searchList: List<Any> = listOf()


    class SearchResultItemViewHolder(private val binding: ViewholderSearchResultItemBinding, val searchResultClickListener: (Any) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(data: Any) = with(binding) {
            textTextView.text = "제목"
            subTextTextView.text = "부제목"

        }

        fun bindViews(data: Any) {
            binding.root.setOnClickListener{
                searchResultClickListener(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultItemViewHolder {
        val view = ViewholderSearchResultItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchResultItemViewHolder(view, searchResultClickListener)
    }

    override fun onBindViewHolder(holder: SearchResultItemViewHolder, position: Int) {
        holder.bindData(Any())
        holder.bindViews(Any())
    }

    override fun getItemCount(): Int = 10


}