package com.inzynierka.monitoring.ui.sensorreadings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi

class SensorReadingsViewModelFactory(private val sensorName: String) : ViewModelProvider.NewInstanceFactory() {
    @ExperimentalCoroutinesApi
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = SensorReadingsViewModel(sensorName) as T
}