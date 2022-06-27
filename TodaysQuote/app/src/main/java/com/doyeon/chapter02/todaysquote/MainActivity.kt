package com.doyeon.chapter02.todaysquote

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import org.json.JSONArray
import org.json.JSONObject
import kotlin.math.absoluteValue

class MainActivity : AppCompatActivity() {

    private val viewPager: ViewPager2 by lazy {
        findViewById(R.id.viewPager)
    }
    private val progreeBar: ProgressBar by lazy {
        findViewById(R.id.progressBar)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initData()
        initViews()
    }



    private fun initData() {
        val remoteConfig = Firebase.remoteConfig

        remoteConfig.setConfigSettingsAsync(
            //비동기
            remoteConfigSettings {
                minimumFetchIntervalInSeconds = 0
                //서버에서 블락하지 않는이상 앱을 들어왔을때 자동으로 fetch 가 된다.
            }
        )
        remoteConfig.fetchAndActivate().addOnCompleteListener {
            progreeBar.visibility = View.GONE
            if (it.isSuccessful) {
                val quotes = parseQuotesJson(remoteConfig.getString("quotes"))
                val isNameRevealed = remoteConfig.getBoolean("is_name_revealed")
                displayQuotesPager(quotes, isNameRevealed)
            }
        }
    }

    private fun initViews() {
        viewPager.setPageTransformer { page, position ->
            when {
                position.absoluteValue >= 1F -> {
                    page.alpha = 0F
                }
                position == 0F -> {
                    page.alpha = 1F
                }
                else -> {
                    page.alpha = 1F - 2 * position.absoluteValue
                }
            }
        }
    }

    private fun parseQuotesJson(json: String): List<Quote> {
        val jsonArray = JSONArray(json)
        var jsonList = emptyList<JSONObject>()
        for (index in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(index)
            jsonObject?.let {
                jsonList = jsonList + it
            }
        }

        return jsonList.map {
            Quote(
                quote = it.getString("quote"),
                name = it.getString("name")
            )

        }
    }

    private fun displayQuotesPager(quotes:List<Quote>, isNameRevealed: Boolean) {

        val adapter = QuotePagerAdapter(
            quotes,
            isNameRevealed = isNameRevealed
        )
        viewPager.adapter = adapter
        viewPager.setCurrentItem(adapter.itemCount / 2, false)
    }
//        listOf(
//            Quote(
//                "나는 생각한다. 고로 존재한다.",
//                "데카르트"
//            )
//        )
//

}