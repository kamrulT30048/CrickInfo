package com.kamrulhasan.topnews.utils

import android.annotation.SuppressLint
import android.util.Log
import java.util.Date
import java.text.SimpleDateFormat
import java.util.*

class DateConverter {

    companion object {

        fun zoneToDate(date: String): String {
            val splitDate = date.split("T", "Z")
            return splitDate[0] //+ " " + splitDate[1]
        }
        fun dateToYear(date: String): Int {
            val splitDate = date.split("-")
            return splitDate[0].toInt()
        }

        fun dateToLong(date: Date): Long {
            return date.time
        }

        @SuppressLint("SimpleDateFormat")
        fun stringToDate(date: String): Date{
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
            format.timeZone = TimeZone.getTimeZone("UTC")
            return format.parse(date)!!
        }

        fun longToDate(time: Long): Date {
            return Date(time)
        }

        fun todayDate(): String {
            val today = Calendar.getInstance()
            val hour = Calendar.HOUR
            val minute = Calendar.MINUTE
            val formatter = SimpleDateFormat("yyyy-MM-dd")
            val todayDate = formatter.format(today.time)
            return "${todayDate}T${hour}:${minute}:00.000000Z"
        }

        fun upcomingThreeMonth(): String {
            val today = Calendar.getInstance()
            val formatter = SimpleDateFormat("yyyy-MM-dd")
            today.add(Calendar.MONTH, 3)
            val lastDate = formatter.format(today.time)
            return "${lastDate}T23:59:00.000000Z"
        }

        fun passedThreeMonth(): String {
            val today = Calendar.getInstance()
            val formatter = SimpleDateFormat("yyyy-MM-dd")
            today.add(Calendar.MONTH, -3)
            val lastDate = formatter.format(today.time)
            Log.d("TAG", "passedThreeMonth: $lastDate")

            return "${lastDate}T00:00:00.000000Z"
        }
    }

}