package com.doyeon.chapter04.photoalbum

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.PackageManagerCompat

class MainActivity : AppCompatActivity() {

    private val addPhotoButton: Button by lazy {
        findViewById(R.id.addPhotoButton)
    }

    private val startPhotoFrameModeButton: Button by lazy {
        findViewById(R.id.startPhotoFrameModeButton)
    }

    private val imageViewList: List<ImageView> by lazy {
        mutableListOf<ImageView>().apply {
            add(findViewById(R.id.photoImageView11))
            add(findViewById(R.id.photoImageView12))
            add(findViewById(R.id.photoImageView13))
            add(findViewById(R.id.photoImageView21))
            add(findViewById(R.id.photoImageView22))
            add(findViewById(R.id.photoImageView23))
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1000 -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    navigatePhotos()
                }
            }
            else -> {
                //
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initAddPhotoButton()
        initStartPhotoFrameModeButton()

    }

    private fun initAddPhotoButton() {
        addPhotoButton.setOnClickListener {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED -> {
                    //todo 권한이 부여가 되었을 때 갤러리에서 사진 선택가능
                    navigatePhotos()
                } shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE) ->{
                    //todo 교육용 팝업 확인 후 권한 팝업을 띄우는 기능
                }
                else -> {
                    requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1000)
                }
            }
        }
    }

    private fun showPermissionContextPopup() {
          AlertDialog.Builder(this)
              .setTitle("권한이 필요합니다.")
              .setMessage("전자액자에 앱에서 사진을 불러오 위해 권한이 필요합니다.")
              .setPositiveButton("동의하기", { dialog, which ->
                  requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1000)
              })
              .setNegativeButton("취소하기") { _, _ -> }
              .create()
              .show()
    }

    private fun navigatePhotos() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, 2000)
        //https://bacassf.tistory.com/104
    }

    private fun initStartPhotoFrameModeButton() {

    }
}