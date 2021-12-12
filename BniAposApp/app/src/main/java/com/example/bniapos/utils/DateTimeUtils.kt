package com.example.bniapos.utils

import java.text.SimpleDateFormat
import java.util.*

object DateTimeUtils {


    fun getCurrentDateTimeYYMMDDHHMMSS(): String {
        val dateFormat = SimpleDateFormat("yyMMddHHmmss")
        return dateFormat.format(Date())
    }


    fun getCurrentDateTimeYYYYMMDDHHMMssSSSSS(): String {
        val dateFormat = SimpleDateFormat("yyyyMMddHHmmssSSSSS")
        return dateFormat.format(Date())
    }
}