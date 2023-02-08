package com.kamrulhasan.topnews.utils

class DateConverter {

    companion object {

        fun zoneToDate(date: String): String {
            val splitDate = date.split("T", "Z")
            return splitDate[0] //+ " " + splitDate[1]
        }
    }

}