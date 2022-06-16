package com.doyeon.chapter03.myapplication

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.widget.addTextChangedListener

class DiaryActivity: AppCompatActivity() {

    private val handler = Handler(Looper.getMainLooper()) //메인쓰레드에 연결된 핸들러.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary) //menifest에 항상 등록을 해줘야한다.

         val diaryEditText = findViewById<EditText>(R.id.diaryEditText)
        val detailPreferences = getSharedPreferences("diary", Context.MODE_PRIVATE)

        diaryEditText.setText(detailPreferences.getString("detail", ""))

        val runnable = Runnable {
            getSharedPreferences("diary", Context.MODE_PRIVATE).edit {
                putString("detail",diaryEditText.text.toString())
            }

            Log.d("DiaryActivity","Saved :: ${diaryEditText.text.toString()}")
        }

        diaryEditText.addTextChangedListener {
            //글씨가 바꼈을때
            Log.d("DiaryActivity","TextChanged :: $it")
            handler.removeCallbacks(runnable) //1000 -> 1초, pending 되어있는 핸들러가 있을수도 있기때문에 remove 해준다
            handler.postDelayed(runnable, 500)
        }
    }
}