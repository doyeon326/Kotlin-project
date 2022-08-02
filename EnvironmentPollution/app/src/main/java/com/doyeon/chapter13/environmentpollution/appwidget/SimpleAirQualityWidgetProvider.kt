package com.doyeon.chapter13.environmentpollution.appwidget

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import androidx.work.OneTimeWorkRequest
import com.doyeon.chapter13.environmentpollution.R
import com.doyeon.chapter13.environmentpollution.data.Repository
import com.doyeon.chapter13.environmentpollution.data.models.airquality.Grade
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch

class SimpleAirQualityWidgetProvider: AppWidgetProvider() {


    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)



//        ContextCompat.startForegroundService(
//            context,
//            Intent(context, UpdateWidgetService::class.java)
//        )
//

//
//        Builder(BackupWorker::class.java).addTag("BACKUP_WORKER_TAG").build()
//        WorkManager.getInstance(context).enqueue(request)

        val serviceIntent = Intent(context, UpdateWidgetService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
           // val myWorkRequest = OneTimeWorkRequest.from(MyWork::class.java)
            Log.d("SimpleAirQualityWidgetProvider. ${context.toString()}", "onUpdate")


        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent)
        } else {
            context.startService(serviceIntent)
        }
    }

    class UpdateWidgetService: LifecycleService() {
        override fun onCreate() {
            super.onCreate()
            Log.d("SimpleAirQualityWidgetProvider", "UpdateWidgetService")
            createChannelIfNeeded()

            startForeground(
                NOTIFICATION_ID,
                createNotification()
            )
        }

        override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                val updateViews =  RemoteViews(packageName,R.layout.widget_simple).apply{
                    setTextViewText(
                        R.id.resultTextView,
                        "권한없음"
                    )

                    setViewVisibility(R.id.labelTextView, View.GONE)
                    setViewVisibility(R.id.gradeLabelTextView, View.GONE)
                }
                updateWidget(updateViews)
                stopSelf()
                return super.onStartCommand(intent, flags, startId)
            }
            LocationServices.getFusedLocationProviderClient(this).lastLocation
                .addOnSuccessListener { location ->
                    //todo
                    lifecycleScope.launch{

                        try {
                            val nearbyMonitoringStation = Repository.getNearbyMonitoringStation(
                                location.latitude,
                                location.longitude
                            )
                            val measuredValue =
                                Repository.getLatestAirQualityData(nearbyMonitoringStation!!.stationName!!)
                            val updateViews =
                                RemoteViews(packageName, R.layout.widget_simple).apply {
                                    setViewVisibility(R.id.labelTextView, View.VISIBLE)
                                    setViewVisibility(R.id.gradeLabelTextView, View.VISIBLE)

                                    val currentGrade = (measuredValue?.khaiGrade ?: Grade.UNKNOWN)
                                    setTextViewText(R.id.resultTextView, currentGrade.emoji)
                                    setTextViewText(R.id.gradeLabelTextView, currentGrade.label)
                                }

                            updateWidget(updateViews)

                        }catch (exception: Exception) {
                            exception.printStackTrace()
                        } finally {
                            stopSelf()
                        }

                    }
                }

            return super.onStartCommand(intent, flags, startId)
        }

        override fun onDestroy() {
            super.onDestroy()
            stopForeground(true)
        }

        private fun createChannelIfNeeded() {
            Log.d("SimpleAirQualityWidgetProvider", "createChnnelIfNeeded")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Log.d("SimpleAirQualityWidgetProvider", "version.sdk: ${Build.VERSION.SDK_INT}")
                (getSystemService(NOTIFICATION_SERVICE) as? NotificationManager)
                    ?.createNotificationChannel(
                        NotificationChannel(
                            WIDGET_REFRESH_CHANNEL_ID,
                            "위젯 갱신 채널",
                            NotificationManager.IMPORTANCE_LOW
                        )
                    )
            }
        }

        private fun createNotification(): Notification =
            NotificationCompat.Builder(this, WIDGET_REFRESH_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_refresh_24)
                .build()


        private fun updateWidget(updateViews: RemoteViews) {
            val widgetProvider = ComponentName(this, SimpleAirQualityWidgetProvider::class.java)
            AppWidgetManager.getInstance(this).updateAppWidget(widgetProvider, updateViews)
        }
    }

    companion object {
        private const val NOTIFICATION_ID = 12345
        private const val WIDGET_REFRESH_CHANNEL_ID = "WIDGET_REFRESH"
    }
}

