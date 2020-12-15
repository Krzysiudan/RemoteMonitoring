package com.inzynierka.monitoring.services

import android.util.Log
import com.google.firebase.database.*
import com.inzynierka.monitoring.data.SensorReading
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

object FirebaseSensorReadingService {
    private const val TAG = "FirebaseService"

    @ExperimentalCoroutinesApi
    fun getSensorReadings(sensorName: String): Flow<List<SensorReading>> {
        val db :FirebaseDatabase
        return callbackFlow {
            var sensorPath : DatabaseReference? = null

            try{
                sensorPath = FirebaseDatabase.getInstance().reference.child(sensorName)
            }catch (e: Throwable){
                Log.e(TAG, "Error initialization firebase ",e )
                close(e)
            }

            val readingsChildEventListener = object :ChildEventListener{
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val sensorReading = snapshot.value as SensorReading
                    offer(sensorReading)
                }
                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onChildRemoved(snapshot: DataSnapshot) {}
                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

                override fun onCancelled(error: DatabaseError) {
                    cancel("Error fetching readings", error as Throwable)
                    return
                }

            }

            sensorPath?.addChildEventListener(readingsChildEventListener)
            awaitClose{
                Log.d(TAG, "Canceling sensor readings listener")
                sensorPath?.removeEventListener(readingsChildEventListener)
            }
        }
    }
}
