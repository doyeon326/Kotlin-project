package com.doyeon.chapter06.pomodorotimer

import android.annotation.SuppressLint
import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.SeekBar
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private val remainMinutesTextView: TextView by lazy {
        findViewById(R.id.remainMinutesTextView)
    }

    private val seekBar: SeekBar by lazy {
        findViewById(R.id.seekBar)
    }

    private val remainSecondsTextView: TextView by lazy {
        findViewById(R.id.remainSecondsTextView)
    }

    private val soundPool = SoundPool.Builder().build()
    private var currentCountDownTimer: CountDownTimer? = null
    private var tickingSoundId: Int? = null
    private var bellSoundId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bindViews()
        initSounds()
    }

    override fun onResume() {
        super.onResume()
        soundPool.autoResume()
        //auto가 아닐경우에는 specific 한 id만 control이 가능하다.
    }

    override fun onPause() {
        super.onPause()
        soundPool.autoPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        soundPool.release()
    }

    private fun bindViews() {
        seekBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    if (fromUser) {
                        updateRemainingTime(progress * 60 * 1000L)
                    }

                }

                override fun onStartTrackingTouch(p0: SeekBar?) {
                    stopCountDown()
                }

                override fun onStopTrackingTouch(p0: SeekBar?) {
                    seekBar ?: return
                    if( seekBar.progress == 0 ) {
                        stopCountDown()
                    } else {
                        startCountDown()
                    }

                }
            }
        )
    }

    private fun createCountDownTimer(initialMillis: Long) =

        object: CountDownTimer(initialMillis, 1000L) {
            override fun onTick(p0: Long) {
                //매 1초마다 UI를 갱신해야한다.
                updateRemainingTime(p0)
                updateSeekBar(p0)
            }

            override fun onFinish() {
                completeCountDown()
            }
        }

    private fun completeCountDown() {
        updateRemainingTime(0)
        updateSeekBar(0)

        //벨소리 작업
        soundPool.autoPause() //tickingsound
        bellSoundId?.let { soundId ->
            soundPool.play(soundId, 1F, 1F, 0, 0, 1F)
        }
    }

    private fun startCountDown() {
        currentCountDownTimer = createCountDownTimer(seekBar.progress * 60 * 1000L)
        currentCountDownTimer?.start()
        Log.d("MainActivity", "onStopTrackingTouch")
        tickingSoundId?.let { soundId ->
            Log.d("MainActivity", "optional binding")
            soundPool.play(soundId, 1F, 1F, 0, -1, 1F)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateRemainingTime(remainMills: Long) {
        val remainSeconds = remainMills / 1000

        remainMinutesTextView.text = "%02d'".format(remainSeconds / 60)
        remainSecondsTextView.text = "%02d".format(remainSeconds % 60)
    }

    private fun updateSeekBar(remainMills: Long) {
        seekBar.progress = (remainMills / 1000 / 60).toInt()
    }

    private fun initSounds() {
        Log.d("MainActivity", "initSounds")
        tickingSoundId = soundPool.load(this, R.raw.timer_ticking, 1)
        bellSoundId = soundPool.load(this, R.raw.timer_bell, 1)
    }

    private fun stopCountDown() {
        soundPool.autoPause()
        currentCountDownTimer?.cancel()
        currentCountDownTimer = null
    }

}