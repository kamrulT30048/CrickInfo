package com.kamrulhasan.topnews.utils

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.kamrulhasan.crickinfo.R

//By Default Image
const val IMAGE_URL = "https://www.google.com/imgres?imgurl=https%3A%2F%2Fvectorportal.com%2Fstorage%2Ftennis-ball-on-fire-vector_8379.jpg&imgrefurl=https%3A%2F%2Fvectorportal.com%2Fvector%2Ftennis-ball-on-fire-vector.ai%2F6883&tbnid=HBE9AZ-skCEvnM&vet=12ahUKEwiD2LGGvYX9AhXkitgFHfG6AdcQMygHegQIARBN..i&docid=so-ii3tHemQQJM&w=660&h=660&q=cricket%20icon&hl=en&ved=2ahUKEwiD2LGGvYX9AhXkitgFHfG6AdcQMygHegQIARBN"

//Web View Fragment
const val URL_KEY = "NEWS_URL_KEY"
const val DEFAULT_NEWS_PAGE = "https://www.bbc.com/bengali"

const val VIEW_PAGER_COUNTER = 7

////  API url  \\\\
// Base api Link
const val BASE_URL = "https://cricket.sportmonks.com/api/v2.0/"

/// API Key
const val API_TOKEN = "dH8NTCnnxtGtW3ev2FSfqOaq4jcTWToNZ6r7AXW7RS8TWgekkd270L2jStLT"
//BuildConfig.apiKey


// specific api link
const val FIXTURES_API = "fixtures"             //?api_token=$api_token"



// internet connectivity error massage
const val CHECK_INTERNET = "Please check your internet connection to reload!!"


// Notification Channel constants

// Name of Notification Channel for verbose notifications of background work
@JvmField
val VERBOSE_NOTIFICATION_CHANNEL_NAME: CharSequence =
    "Verbose WorkManager Notifications"
const val VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION =
    "Shows notifications whenever work starts"

@JvmField
val NOTIFICATION_TITLE: CharSequence = "WorkRequest Starting"
const val CHANNEL_ID = "VERBOSE_NOTIFICATION"
const val NOTIFICATION_ID = 1

private const val TAG = "Constrains"

/// network check
@SuppressLint("MissingPermission")
fun verifyAvailableNetwork(activity: Context): Boolean {
    val connectivityManager = activity.getSystemService(Context.CONNECTIVITY_SERVICE)
            as ConnectivityManager
    val networkInfo = connectivityManager.activeNetworkInfo

    return networkInfo != null && networkInfo.isConnected
}

/// notification
@SuppressLint("MissingPermission")
fun makeStatusNotification(message: String, context: Context) {

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
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

        notificationManager?.createNotificationChannel(channel)
    }

    Log.d(TAG, "makeStatusNotification: $message")
    // Create the notification
    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle(NOTIFICATION_TITLE)
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setVibrate(LongArray(0))

    // Show the notification
    try {
        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build())
    } catch (e: Exception) {
        Log.d(TAG, "makeStatusNotification: $e")
    }
}
