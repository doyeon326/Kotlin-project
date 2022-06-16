package com.doyeon.chapter03.myapplication

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.edit

class MainActivity : AppCompatActivity() {

    private val numberPicker1: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.numberPicker1)
            .apply {
                minValue = 0
                maxValue = 9
            }
    }

    private val numberPicker2: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.numberPicker2)
            .apply {
                minValue = 0
                maxValue = 9
            }
    }

    private val numberPicker3: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.numberPicker3)
            .apply {
                minValue = 0
                maxValue = 9
            }
    }

    private val openButton: AppCompatButton by lazy {
        findViewById<AppCompatButton>(R.id.openButton)
    }

    private val changePasswordButton: AppCompatButton by lazy {
        findViewById<AppCompatButton>(R.id.changePassword)
    }

    private var changePasswordMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //호출 하는 이유는 apply 에서 초기 minValue 와 maxValue 를 설정해주기 때문이다. 안그러면 사용하는시점에만
        //불리기 때문에 설정이 안된다.
        numberPicker3
        numberPicker1
        numberPicker1

        openButton.setOnClickListener{
            //내부 파일에 저장
            if (changePasswordMode) {
                Toast.makeText(this, "비밀번호 변경 중입니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val passwordPreferences = getSharedPreferences("password", Context.MODE_PRIVATE)
            val passwordFromUser = "${numberPicker1.value}${numberPicker2.value}${numberPicker3.value}"

            if (passwordPreferences.getString("password","000").equals(passwordFromUser)) {
                //패스워드 성공
                //TODO 다이어리 페이지 작성 후에 넘겨줘야함
                Log.d("MainActivity", "saved password: ${passwordPreferences.getString("password","000")} user password ${passwordFromUser}" )
            } else {
                //실패
                Log.d("MainActivity", "saved password: ${passwordPreferences.getString("password","000")} user password ${passwordFromUser}" )
                showErrorAlertDialog()

            }
        }

        changePasswordButton.setOnClickListener{
            val passwordPreferences = getSharedPreferences("password", Context.MODE_PRIVATE)
            val passwordFromUser = "${numberPicker1.value}${numberPicker2.value}${numberPicker3.value}"
            Log.d("MainActivity", "saved password: ${passwordPreferences.getString("password","000")} user password ${passwordFromUser}" )
            if(changePasswordMode) {
                //번호를 저장하는 기능
                 passwordPreferences.edit(true) {
                     putString("password", passwordFromUser)
                 //passwordPreferences.edit {
                 // .....
                 // commit()
                 // } 과 같음 = 잠시 block 시켰다가 저장후 코드 실행, apply = 바로 실행
                 }
                changePasswordMode = false
                changePasswordButton.setBackgroundColor(Color.BLACK)
            } else {
                //changePasswordMode가 활성화 :: 비밀번호가 맞는지를 체크
                if (passwordPreferences.getString("password","000").equals(passwordFromUser)) {
                    changePasswordMode = true
                    Toast.makeText(this, "변경할 패스워드를 입력해주세요", Toast.LENGTH_SHORT).show()
                    changePasswordButton.setBackgroundColor(Color.RED)
                } else {
                    //실패

                    showErrorAlertDialog()
                }
            }

        }
    }


    private fun showErrorAlertDialog() {
        AlertDialog.Builder(this)
            .setTitle("실패!!")
            .setMessage("비밀번호가 잘못되었습니다")
            .setPositiveButton("확인") { dialog, which ->

            }
            .create()
            .show()
    }

}