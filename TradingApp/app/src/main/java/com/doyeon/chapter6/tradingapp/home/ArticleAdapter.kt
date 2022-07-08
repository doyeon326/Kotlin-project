package com.doyeon.chapter6.tradingapp.home


import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.doyeon.chapter6.tradingapp.databinding.ItemArticleBinding
import java.util.*

class ArticleAdapter(val onItemClicked: (ArticleModel) -> Unit): ListAdapter<ArticleModel, ArticleAdapter.ViewHolder>(diffUtil) {
        inner class ViewHolder(private val binding: ItemArticleBinding): RecyclerView.ViewHolder(binding.root) {
                fun bind(articleNModel: ArticleModel) {

                        val format = SimpleDateFormat("MM월 dd일")
                        val date = Date(articleNModel.createdAt)


                        binding.titleTextView.text = articleNModel.title
                        binding.dateTextView.text = format.format(date).toString()
                        binding.priceTextView.text = articleNModel.price


                        if (articleNModel.imageUrl.isNotEmpty()) {
                                Glide.with(binding.thumbnailImageView)
                                        .load(articleNModel.imageUrl)
                                        .into(binding.thumbnailImageView)
                        }

                        binding.root.setOnClickListener {
                                onItemClicked(articleNModel)
                        }

                }


        }

        override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
        ): ArticleAdapter.ViewHolder {
                return  ViewHolder(ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }

        override fun onBindViewHolder(holder: ArticleAdapter.ViewHolder, position: Int) {
                holder.bind(currentList[position])
        }


        companion object {
                val diffUtil = object: DiffUtil.ItemCallback<ArticleModel>() {
                        override fun areItemsTheSame(
                                oldItem: ArticleModel,
                                newItem: ArticleModel
                        ): Boolean {
                                return oldItem.createdAt == newItem.createdAt
                        }

                        override fun areContentsTheSame(
                                oldItem: ArticleModel,
                                newItem: ArticleModel
                        ): Boolean {
                               return oldItem == newItem
                        }

                }
        }
}
