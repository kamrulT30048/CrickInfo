package com.kamrulhasan.topnews.utils

import android.annotation.SuppressLint
import android.util.Log
import java.util.Date
import java.text.SimpleDateFormat
import java.util.*

class DateConverter {

    companion object {

        fun zoneToDate(date: String): String {
            val splitDate = date.split("T")
            return splitDate[0]
        }

        fun zoneToTime(date: String): String {
            val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault())
            dateFormatter.timeZone = TimeZone.getTimeZone("GMT")
            val dateIntLong = dateFormatter.parse(date)?.time
            val date = dateIntLong?.let { Date(it) }

            return "${date?.hours}:${date?.minutes}"
        }

        fun zoneToDateTime(date: String): String {
            val splitDate = date.split("T", "Z", ".")
            return splitDate[0] + " " + splitDate[1]
        }

        fun dateToYear(date: String): Int {
            val splitDate = date.split("-")
            return splitDate[0].toInt()
        }

        fun dateToLong(date: Date): Long {
            return date.time
        }

        @SuppressLint("SimpleDateFormat")
        fun stringToDateLong(date: String): Long {
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault())
            format.timeZone = TimeZone.getTimeZone("GMT")
            var time :Long = 0
            time = format.parse(date)?.time ?: 0
            return  time
        }

        fun londonToDhakaTimeZone(dateStr: String){

        }

        @SuppressLint("SimpleDateFormat")
        fun todayDateToLong(): Long {
            val today = Calendar.getInstance()
            return  today.time.time
        }

        fun longToDate(time: Long): Date {
            return Date(time)
        }

        fun todayDateWithTimeZone(): String {
            val today = Calendar.getInstance()
            val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
            dateFormatter.timeZone = TimeZone.getTimeZone("GMT")
            return dateFormatter.format(today.time)
        }

        fun todayDateForRecentTimeZone(): String {
            val today = Calendar.getInstance()
            val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
            dateFormatter.timeZone = TimeZone.getTimeZone("GMT-04:00")
            return dateFormatter.format(today.time)
        }

        fun customDateForLiveTimeZone(zone: String): String {
            val today = Calendar.getInstance()
            val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
            dateFormatter.timeZone = TimeZone.getTimeZone("GMT${zone}:00")
            return dateFormatter.format(today.time)
        }



        fun upcomingTwoMonth(): String {
            val today = Calendar.getInstance()
            today.add(Calendar.MONTH, 2)
            val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
            dateFormatter.timeZone = TimeZone.getTimeZone("GMT")
            return dateFormatter.format(today.time)
        }

        fun upcomingTwoWeek(): String {
            val today = Calendar.getInstance()
            today.add(Calendar.DAY_OF_MONTH, 15)
            val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
            dateFormatter.timeZone = TimeZone.getTimeZone("GMT")
            return dateFormatter.format(today.time)
        }

        fun passedTwoMonth(): String {
            val today = Calendar.getInstance()
            today.add(Calendar.MONTH, -2)
            val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
            dateFormatter.timeZone = TimeZone.getTimeZone("GMT")
            return dateFormatter.format(today.time)
        }
    }

}