package com.inzynierka.monitoring.services

import android.util.Log
import com.github.mikephil.charting.data.Entry
import com.google.firebase.database.*
import com.inzynierka.monitoring.data.SensorReading
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import okhttp3.Dispatcher
import kotlin.coroutines.CoroutineContext

@ExperimentalCoroutinesApi
class FirebaseSensorReadingService(val sensorName: String = "") {
    private val TAG = "FirebaseService"
    val referenceTimeStampFlow: Flow<Long> = callbackFlow {
            var sensorPath : DatabaseReference? = null
            try{
                sensorPath = FirebaseDatabase.getInstance().reference.child(sensorName)
            }catch (e: Throwable){
                Log.e(TAG, "Error initialization firebase ",e )
            }
            val firstTimeStampQuery = sensorPath?.orderByChild("time")?.limitToFirst(1)

            val firstTimeStampListener = object : ChildEventListener{
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    Log.d(TAG, "getFirstTimeStampForReference onChildAdded: DataSnapshot : ${snapshot.toString()}, previousChildName: $previousChildName")
                    val sensorReading = snapshot.getValue(SensorReading::class.java)
                    Log.d(TAG, "getFirstTimeStampForReference onChildAdded: sensorReading - $sensorReading")
                    val  firstTimeStamp: Long? = sensorReading?.time
                    Log.d(TAG, "getFirstTimeStampForReference onChildAdded: firstTimeStamp - $firstTimeStamp")
                    offer(firstTimeStamp)

                }
                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onChildRemoved(snapshot: DataSnapshot) {}
                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onCancelled(error: DatabaseError) {}
            }
            firstTimeStampQuery?.addChildEventListener(firstTimeStampListener)
            awaitClose {
                firstTimeStampQuery?.removeEventListener(firstTimeStampListener)
            }
    }

    @ExperimentalCoroutinesApi
    fun getSensorReadings(referenceTimeStamp: Long?): Flow<List<Entry>> {

        return callbackFlow {
            var sensorPath : DatabaseReference? = null
            val entryArray:  ArrayList<Entry> = ArrayList<Entry>()

            try{
                sensorPath = FirebaseDatabase.getInstance().reference.child(sensorName)
            }catch (e: Throwable){
                Log.e(TAG, "Error initialization firebase ",e )
                close(e)
            }


                    val readingsChildEventListener = object :ChildEventListener{
                        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                            Log.d(TAG, "getSensorReadings onChildAdded: : $snapshot")

                            val sensorReading  = snapshot.getValue(SensorReading::class.java)
                            Log.d(TAG, "getSensorReadings onChildAdded sensorReading: $sensorReading")

                            Log.d(TAG, "getSensorReadings onChildAdded firstTimeStamp: $referenceTimeStamp")
                            if(referenceTimeStamp!=null && sensorReading !=null){
                                val subtractedTime: Float = ((sensorReading.time)-referenceTimeStamp).toFloat()
                                Log.d(TAG, "getSensorReadings onChildAdded: subtractedTime : $subtractedTime ")
                                val timestamp: Float = String.format("%.7f",subtractedTime).toFloat().div(1000F)
                                Log.d(TAG, "getSensorReadings onChildAdded: timestamp : $timestamp ")
                                val entryDataChart: Entry = Entry(timestamp,sensorReading.value.toFloat())
                                Log.d(TAG, "getSensorReadings onChildAdded entryDataChart: $entryDataChart")
                                entryArray.add(entryDataChart)
                                offer(entryArray)
                            }

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



    fun getFirstTimeStampForReference(sensorName: String){
        var sensorPath : DatabaseReference? = null
        var firstTimeStamp : Long? = null

        try{
            sensorPath = FirebaseDatabase.getInstance().reference.child(sensorName)
        }catch (e: Throwable){
            Log.e(TAG, "Error initialization firebase ",e )
        }

        val firstTimeStampQuery = sensorPath?.orderByChild("time")?.limitToFirst(1)

        val firstTimeStampListener = object : ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d(TAG, "getFirstTimeStampForReference onChildAdded: DataSnapshot : ${snapshot.toString()}, previousChildName: $previousChildName")
                val sensorReading = snapshot.getValue(SensorReading::class.java)
                Log.d(TAG, "getFirstTimeStampForReference onChildAdded: sensorReading - $sensorReading")
                firstTimeStamp = sensorReading?.time
                Log.d(TAG, "getFirstTimeStampForReference onChildAdded: firstTimeStamp - $firstTimeStamp")
                /*
               val map = HashMap<String,SensorReading>()
                for((key,value) in map){
                    firstTimeStamp = value.timestamp.toInt()
                }
                Log.d(TAG, "Fun getFirstTimeStampForReference - timestamp: $firstTimeStamp ")
*/
            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}

        }

        firstTimeStampQuery?.addChildEventListener(firstTimeStampListener)


    }
}
