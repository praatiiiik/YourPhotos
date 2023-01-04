package com.yourphotos.utils

import android.content.Context
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*

object HelperFunction {
    fun epochToIso8601(time: Long): String? {
        val format = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        sdf.timeZone = TimeZone.getDefault()
        return sdf.format(Date(time * 1000))
    }

    fun showToast(msg:String,context: Context){
        Toast.makeText(context,msg, Toast.LENGTH_SHORT).show()
    }
}