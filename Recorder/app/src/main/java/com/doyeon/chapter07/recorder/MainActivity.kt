package com.doyeon.chapter07.recorder

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {

    private val recordButton: RecordButton by lazy {
        findViewById(R.id.recordButton)
    }
    private var state = State.BEFORE_RECORDING
    private var requiredPermission = arrayOf(android.Manifest.permission.RECORD_AUDIO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requerstAudioPermission()
        initViews()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val audioRecordPermissionGranted = requestCode == REQUEST_RECORD_AUDIO_PERMISSION &&
                grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED

        if(!audioRecordPermissionGranted) {
            finish() //앱종료
        }
    }

    private fun requerstAudioPermission() {
        requestPermissions(requiredPermission, REQUEST_RECORD_AUDIO_PERMISSION)
    }

    private fun initViews() {
        recordButton.updateIconWithState(state)
    }

    companion object {
        //A companion object is initialized when the corresponding
        // class is loaded (resolved), matching the semantics of a Java static initializer.
        // Means that the companion object will be initialized even before calling the constructor
        // of that class as similar to Java static initializer.

        private const val REQUEST_RECORD_AUDIO_PERMISSION = 201
    }

}