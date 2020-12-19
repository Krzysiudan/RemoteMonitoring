package com.inzynierka.monitoring.util.charts

import android.util.Log
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter
import java.lang.Exception
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class AxisValueFormatter(
    private val referenceTimeStamp: Long,
    private val dataFormat: DateFormat = SimpleDateFormat("HH:mm", Locale.getDefault()),
    private val date: Date = Date()
) : ValueFormatter() {
    private val TAG = "AxisValueFormatter"

    override fun getFormattedValue(value: Float): String {
        Log.d(TAG, "getFormattedValue: value : $value ")

        val convertedTimeStamp: Long = value.toLong()

        val originalTimeStamp: Long = (convertedTimeStamp + referenceTimeStamp)

        return getHour(originalTimeStamp)
    }

    private fun getHour(timeStamp: Long): String {
        return try{
            date.time = timeStamp*1000
            dataFormat.format(date)
        }catch (e : Exception){
            "xx"
        }

    }
}