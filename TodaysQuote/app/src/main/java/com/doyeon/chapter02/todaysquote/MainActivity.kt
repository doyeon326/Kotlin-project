package com.doyeon.chapter02.todaysquote

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private val viewPager: ViewPager2 by lazy {
        findViewById(R.id.viewPager)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initData()
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
            if (it.isSuccessful) {
                Log.d("MainActivity", "successful")
                val quotes = parseQuotesJson(remoteConfig.getString("quotes"))
                val isNameRevealed = remoteConfig.getBoolean("is_name_revealed")
                displayQuotesPager(quotes, isNameRevealed)
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
        Log.d("MainActivity", "jsonList ${jsonList}, count ${jsonList.count()}")
    }

    private fun displayQuotesPager(quotes:List<Quote>, isNameRevealed: Boolean) {
        viewPager.adapter = QuotePagerAdapter(
            quotes,
            isNameRevealed = isNameRevealed
        )
    }
//        listOf(
//            Quote(
//                "나는 생각한다. 고로 존재한다.",
//                "데카르트"
//            )
//        )
//

}