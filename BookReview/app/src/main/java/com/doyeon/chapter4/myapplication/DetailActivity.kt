package com.doyeon.chapter4.myapplication

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.doyeon.chapter4.myapplication.databinding.ActivityDetailBinding
import com.doyeon.chapter4.myapplication.model.Book
import com.doyeon.chapter4.myapplication.model.Review

class DetailActivity: AppCompatActivity() {
    private lateinit var db: AppDatabase
    private lateinit var binding: ActivityDetailBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        Log.d("DetailActivity", "onCreate() ")
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        db = getAppDatabase(this)

        val model = intent.getParcelableExtra<Book>("bookModel")
        binding.titleTextView.text = model?.title.orEmpty()
        binding.descriptionTextView.text = model?.description.orEmpty()

        Glide.with(binding.coverImageView.context)
            .load(model?.coverSmallUrl.orEmpty())
            .into(binding.coverImageView)

        Thread{
            val review = db.reviewDao().getOneReview(model?.id?.toInt() ?: 0)
            runOnUiThread {
                binding.reviewEditText.setText(review.review.orEmpty())
            }
        }.start()

        binding.saveButton.setOnClickListener {
            Thread {
                db.reviewDao().saveReview(
                    Review(model?.id?.toInt() ?: 0 ,
                        binding.reviewEditText.text.toString()
                    )
                )

            }.start()
        }

    }

}