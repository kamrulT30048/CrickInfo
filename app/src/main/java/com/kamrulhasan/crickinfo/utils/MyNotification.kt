package com.kamrulhasan.crickinfo.utils

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.kamrulhasan.crickinfo.R
import com.kamrulhasan.crickinfo.model.fixture.FixturesData
import com.kamrulhasan.crickinfo.ui.MainActivity
import com.kamrulhasan.crickinfo.viewmodel.CrickInfoViewModel
import com.kamrulhasan.topnews.utils.*
import java.util.*

private const val TAG = "Notification"

class MyNotification {
    companion object {

        ///////////////////////////////
        /////  make notification  /////
        ///////////////////////////////

//        @RequiresApi(Build.VERSION_CODES.M)
//        @RequiresApi(Build.VERSION_CODES.S)
        @SuppressLint("MissingPermission", "UnspecifiedImmutableFlag")
        fun makeStatusNotification(message: String) {

            val notificationIntent = Intent(MyApplication.appContext, MainActivity::class.java)
            notificationIntent.flags =
                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            val contentIntent = PendingIntent.getActivity(
                MyApplication.appContext,
                0,
                notificationIntent,
                PendingIntent.FLAG_MUTABLE
            )

            // Make a channel if necessary
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Create the NotificationChannel, but only on API 26+ because
                // the NotificationChannel class is new and not in the support library
                val name = VERBOSE_NOTIFICATION_CHANNEL_NAME
                val description = VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION
                val importance = NotificationManager.IMPORTANCE_HIGH
                val channel = NotificationChannel(CHANNEL_ID, name, importance)
                channel.description = description

                // Add the channel
                val notificationManager =
                    MyApplication.appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

                notificationManager?.createNotificationChannel(channel)
            }

            Log.d(TAG, "makeStatusNotification: $message")
            // Create the notification
            val builder = NotificationCompat.Builder(MyApplication.appContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.icon_match)
                .setContentTitle(NOTIFICATION_TITLE)
                .setContentText(message)
                .setContentIntent(contentIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(LongArray(0))
                .setAutoCancel(true)

            // Show the notification
            try {
                NotificationManagerCompat.from(MyApplication.appContext).notify(NOTIFICATION_ID, builder.build())
            } catch (e: Exception) {
                Log.d(TAG, "makeStatusNotification: $e")
            }
        }

        ////////////////////////////////////////
        /////  set Alarm for notification  /////
        ////////////////////////////////////////

//        @RequiresApi(Build.VERSION_CODES.S)
        @SuppressLint("UnspecifiedImmutableFlag")
        fun scheduleNotification(delay: Long, message: String) {

            val intent = Intent(MyApplication.appContext, MyBroadcastReceiver::class.java)
            intent.putExtra("message", message)

            val pendingIntent = PendingIntent.getBroadcast(
                MyApplication.appContext,
                0,
                intent,
                PendingIntent.FLAG_MUTABLE
            )

            val alarmManager =
                MyApplication.appContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            /*val calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                add(Calendar.SECOND, 3)
            }*/
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                delay,
                pendingIntent
            )
        }

        ////////////////////////////////////////
        /// set notification time and message //
        ////////////////////////////////////////
/*
        fun setNotificationTime(
            match: FixturesData,
            viewModel: CrickInfoViewModel,
            viewLifecycleOwner: LifecycleOwner
        ) {

            match.starting_at?.let { it ->
                val timeMillis = DateConverter.stringToDateLong(it)
                var team1 = ""
                var team2 = ""
                viewModel.readTeamCode(match.localteam_id).observe(viewLifecycleOwner){ localCode ->
                    team1 = localCode
                }
                viewModel.readTeamCode(match.visitorteam_id).observe(viewLifecycleOwner){ visitorCode ->
                    team2 = visitorCode
                }

                //handle data loading error
                Handler(Looper.getMainLooper()).postDelayed({
                    if (team1.isNotEmpty() && team2.isNotEmpty()) {
                        val message = "  $team1  VS  $team2"
                        scheduleNotification(timeMillis, message)
                    }
                }, 5000)

            }
        }*/
    }
}