package com.algostack.nir.services.androidservices

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.algostack.nir.R
import com.algostack.nir.utils.Constants.NOTIFICATION_ID
import com.algostack.nir.view.main_frame.Notification
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NoficationForgroundService : Service(){

    // here i want to check getallnotification data if data are changes then i want to show notification

//    @Inject
//    lateinit var notificationViewModel: NotificationViewModel

    private lateinit var notificationManager: NotificationManager

    private val POLL_INTERVAL = 60000L // 1 minute

    private val handler = Handler()

    private val pollingRunnable = object : Runnable {
        override fun run() {
            // here i want to check getallnotification data if data are changes then i want to show notification

           // notificationViewModel.getallNotifications()

            println("service running...")
            handler.postDelayed(this, POLL_INTERVAL)
        }
    }

    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        startForeground(NOTIFICATION_ID, createNotification())
        println("service started")
        handler.postDelayed(pollingRunnable, POLL_INTERVAL)
    }

    private fun createNotification(): android.app.Notification {
        val channelId = "my_channel_id"
        val channelName = "My Channel"
        val notificationId = 1

        // Create Notification channel (required for Android Oreo and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        // Build the notification
        val notificationIntent = Intent(this, Notification::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Notification Title")
            .setContentText("Notification Text")
            .setSmallIcon(R.drawable.notification_icon)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        return notificationBuilder.build()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(pollingRunnable)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }






}