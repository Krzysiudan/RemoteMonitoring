package com.inzynierka.monitoring.data

import android.os.Parcelable
import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.database.DataSnapshot
import com.inzynierka.monitoring.data.SensorReading.Companion.toSensorReading
import kotlinx.parcelize.Parcelize
import java.lang.Exception
import java.sql.Timestamp

@Parcelize
data class SensorReading(val timestamp: Timestamp,
val value :Float) :Parcelable {

    companion object{
        fun DataSnapshot.toSensorReading(): SensorReading?{
            return try{
                val sensorReading = value as SensorReading;
                sensorReading
            }catch ( e : Exception){
                Log.e(TAG, "Error converting sensor reading",e)
                FirebaseCrashlytics.getInstance().log("Error converting sensor reading")
                FirebaseCrashlytics.getInstance().setCustomKey("sensorTimeStamp",(value as SensorReading).value)
                FirebaseCrashlytics.getInstance().recordException(e)
                null
            }
        }
        private const val TAG = "SensorReading"
    }


}