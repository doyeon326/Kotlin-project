package com.doyeon.chapter3.simplealarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //todo: step0 뷰를 초기화해주기
        initOnOffButton()
        initChangeAlarmTimeButton()
        //todo: step1 데이터가져오기

        val model = fetchDataFromSharedPreferences()
        renderView(model)

        //todo: step2 뷰에 데이터를 그려주기
    }

    private fun initOnOffButton() {
            val onOffButton = findViewById<Button>(R.id.onOffButton)
            onOffButton.setOnClickListener {
                //데이터를 확인을 한다.
                val model = it.tag as? AlarmDisplayModel ?: return@setOnClickListener
                val newModel = saveAlarmModel(model.hour, model.minute, !model.onOff)
                renderView(newModel)

                if (newModel.onOff) {
                    //켜진경우 -> 알람을 등록
                    val calendar = Calendar.getInstance().apply {
                        set(Calendar.HOUR_OF_DAY, newModel.hour)
                        set(Calendar.MINUTE, newModel.minute)

                        if (before(Calendar.getInstance())) {
                            add(Calendar.DATE, 1)
                        }
                    }
                    val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    val intent = Intent(this, AlarmReceiver::class.java)
                    val pendingIntent = PendingIntent.getBroadcast(this, ALARM_REQUEST_CODE,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

                    //온 -> 알람을 등록
                    alarmManager.setInexactRepeating(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        AlarmManager.INTERVAL_DAY,
                        pendingIntent
                    )
                } else {
                    //꺼진 경우 -> 알람을 제거
                    //오프 -> 알람을 제거
                    cancleAlarm()
                }
                //온오프에 따라 작업을 처리한다.


                //데이터를 저장한다.
            }
    }

    private fun initChangeAlarmTimeButton() {
        val changeAlarmButton = findViewById<Button>(R.id.changeAlarmTimeButton)
        changeAlarmButton.setOnClickListener {
            //현재시간을 일단 가져온다
            //TimePickDialog 띄어줘서 시간을 설정을 하도록 하게끔 하고, 그 시간을 가져와서
            val calender = Calendar.getInstance()
            TimePickerDialog(this, { picker, hour, minute ->
                val model = saveAlarmModel(hour, minute, false)
                renderView(model)

                cancleAlarm()

            },calender.get(Calendar.HOUR_OF_DAY), calender.get(Calendar.MINUTE), false)
                .show()
            //데이터를 저장한다
            //뷰를 업데이트 한다
        }
    }

    private fun saveAlarmModel(hour: Int, minute: Int, onOff: Boolean): AlarmDisplayModel {
        val model = AlarmDisplayModel(
            hour = hour,
            minute = minute,
            onOff = onOff
        )
        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString(ALARM_KEY, model.makeDataForDB() )
            putBoolean(ONOFF_KEY, model.onOff)
            commit()
        }
        return model
    }

    private fun fetchDataFromSharedPreferences(): AlarmDisplayModel {
        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

        val timeDBValue = sharedPreferences.getString(ALARM_KEY, "9:30") ?: "9:30"
        val onOffDBValue = sharedPreferences.getBoolean(ONOFF_KEY, false)
        val alarmData = timeDBValue.split(":")

        val alarmModel = AlarmDisplayModel(
            hour = alarmData[0].toInt(),
            minute = alarmData[0].toInt(),
            onOff = onOffDBValue
        )

        //예외처리

        val pendingIntent = PendingIntent.getBroadcast(this, ALARM_REQUEST_CODE, Intent(this, AlarmReceiver::class.java),PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE)
        //build version31 이상일경우 IMMUTABLE || MUTABLE 이 무조건 들어가야한다.
        if ((pendingIntent == null) and alarmModel.onOff) {
            // 알람은 꺼져있는데, 데이터는 켜저있는 경우
            alarmModel.onOff = false

        } else if ((pendingIntent != null) and alarmModel.onOff.not()){
            // 알람은 켜져있는데, 데이터는 꺼져있는 경우
            // 알람을 취소함
            pendingIntent.cancel()
        }

        return alarmModel

    }

    private fun renderView(model: AlarmDisplayModel) {
        findViewById<TextView>(R.id.ampmTextView).apply {
            text = model.ampmText
        }
        findViewById<TextView>(R.id.timeTextView).apply {
            text = model.timeText
        }
        findViewById<Button>(R.id.onOffButton).apply {
            text = model.onOffText
            tag = model
        }
    }

    private fun cancleAlarm() {
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            ALARM_REQUEST_CODE,
            Intent(this, AlarmReceiver::class.java), PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE)
        pendingIntent?.cancel()
    }

    companion object {
        private const val ALARM_KEY = "alarm"
        private const val ONOFF_KEY = "onOff"
        private const val SHARED_PREFERENCES_NAME = "time"
        private const val ALARM_REQUEST_CODE = 1000
    }


}