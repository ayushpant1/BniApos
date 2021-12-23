package com.example.bniapos.utils

import java.lang.Exception
import java.text.DateFormat
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

    fun GetDate(DateFormat: String): String {
        return if (DateFormat != null && DateFormat.length == 12) DateFormat[0].toString() + DateFormat[1].toString() +
                "/" + DateFormat[2] + DateFormat[3] + "/" + "20" + DateFormat[4] +
                DateFormat[5] else ""
    }

    fun GetTime(DateFormat: String): String {
        return if (DateFormat != null && DateFormat.length == 12) DateFormat[6].toString() + DateFormat[7].toString() + ":" + DateFormat[8] + DateFormat[9] + ":" + DateFormat[10] + DateFormat[11] else ""
    }

    fun getCurrentDatetimeForExpiry(): String? {
        try {
            val DATE_FORMAT_NOW = "yyMM"
            val calendar = Calendar.getInstance()
            val sdf = SimpleDateFormat(DATE_FORMAT_NOW)
            return sdf.format(calendar.time)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    // get Today date. Return Value as String
    fun getTodayDay(): String? {
        try {
            val date = Date()
            val df: DateFormat = SimpleDateFormat("EEEE, dd/MM/yyyy")
            //Date date = new Date();
            //	DateFormat df = new SimpleDateFormat("EEE").format(new Date());
            /*System.out.println(df.format(date));
			Calendar calendar = Calendar.getInstance();
			String sdf =  new SimpleDateFormat("EEE").format(new Date());*/
            return df.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    // get Today date. Return Value as String
    fun getToday(): String? {
        try {
            val DATE_FORMAT_NOW = "dd/MM/yyyy"
            val calendar = Calendar.getInstance()
            val sdf = SimpleDateFormat(DATE_FORMAT_NOW)
            return sdf.format(calendar.time)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun getYesterdayPlus30Days(): String? {
        try {
            val DATE_FORMAT_NOW = "MM/dd/yyyy"
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, -30)
            val sdf = SimpleDateFormat(DATE_FORMAT_NOW)
            return sdf.format(calendar.time)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    // get Tommorow date . Return Value as String
    fun getTomorrow(): String? {
        try {
            val DATE_FORMAT_NOW = "MM/dd/yyyy"
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_WEEK, 1)
            val sdf = SimpleDateFormat(DATE_FORMAT_NOW)
            return sdf.format(calendar.time)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun getnextTomorrow(): String? {
        try {
            val DATE_FORMAT_NOW = "MM/dd/yyyy"
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_WEEK, 2)
            val sdf = SimpleDateFormat(DATE_FORMAT_NOW)
            return sdf.format(calendar.time)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    // get Tommorow date . Return Value as String
    fun getWeek(): String? {
        try {
            val DATE_FORMAT_NOW = "MM/dd/yyyy"
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, 8)
            val sdf = SimpleDateFormat(DATE_FORMAT_NOW)
            return sdf.format(calendar.time)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null


        /*	Calendar calendar = Calendar.getInstance();
		calendar.setTime(myDate);
		calendar.add(Calendar.DAY_OF_YEAR, -7);
		Date newDate = calendar.getTime();

		String date = dateFormat.format(newDate);*/
    }
}