package com.kamrulhasan.topnews.utils

import java.sql.Date

class DateConverter {

    companion object {

        fun zoneToDate(date: String): String {
            val splitDate = date.split("T", "Z")
            return splitDate[0] //+ " " + splitDate[1]
        }

        fun dateToLong(date: java.util.Date):Long{
            return date.time
        }
        fun longToDate(time:Long): Date{
            return Date(time)
        }
    }

}