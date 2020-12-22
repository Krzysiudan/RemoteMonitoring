package com.inzynierka.monitoring.ui.sensorreadings

import android.util.Log
import androidx.lifecycle.*
import com.github.mikephil.charting.data.Entry
import com.inzynierka.monitoring.services.FirebaseSensorReadingService
import com.inzynierka.monitoring.util.LiveMessageEvent
import com.inzynierka.monitoring.util.SubscribeEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class SensorReadingsViewModel(sensorName: String) : ViewModel() {

    private lateinit var firebaseService: FirebaseSensorReadingService
    private val TAG = "SensorReadingsViewModel"

    val subscribeEvent = LiveMessageEvent<SubscribeEvent>()

    lateinit var sensorReadings :LiveData<List<Entry>>


    init {
        viewModelScope.launch {
            firebaseService = FirebaseSensorReadingService(sensorName)
            firebaseService.referenceTimeStampFlow.collect {
                Log.d(TAG, "referenceTimeStampFlow collect - long : $it ")
               sensorReadings = firebaseService.getSensorReadings(it).asLiveData(viewModelScope.coroutineContext)
                subscribeEvent.sendEvent {
                    subscribeLiveData(it)
                }
            }
        }
    }

}