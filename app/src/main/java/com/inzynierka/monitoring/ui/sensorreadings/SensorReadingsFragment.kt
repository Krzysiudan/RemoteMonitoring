package com.inzynierka.monitoring.ui.sensorreadings

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.inzynierka.monitoring.R
import com.inzynierka.monitoring.util.SubscribeEvent
import com.inzynierka.monitoring.util.charts.AxisValueFormatter
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class SensorReadingsFragment : Fragment(), SubscribeEvent {

    private lateinit var sensorReadingsViewModel: SensorReadingsViewModel
    private val TAG = "SensorReadingFragment"
    private lateinit var lineChart: LineChart
    private lateinit var lineData: LineData
    private lateinit var dataSet: LineDataSet

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        sensorReadingsViewModel =
                ViewModelProvider(this).get(SensorReadingsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_sensor_readings, container, false)
        lineChart = root.findViewById<LineChart>(R.id.graph)
        lineData = LineData()
        sensorReadingsViewModel.subscribeEvent.setEventReceiver(this,this)


        return root
    }


    override fun subscribeLiveData(referenceTimeStamp: Long) {
        val xAxis = lineChart.xAxis
        xAxis.valueFormatter = AxisValueFormatter(referenceTimeStamp)
        sensorReadingsViewModel.sensorReadings.observe(viewLifecycleOwner, Observer {
           /* it.forEach {
                Log.d(TAG, "subscribeLiveData: entry : $it")
            }*/
            if (it != null) {
                if (it.isNotEmpty()) {
                    dataSet = LineDataSet(it, "Temperature")
                    Log.d(TAG, "subscribeLiveData: setting entry to chart")
                    dataSet.setColor(ContextCompat.getColor(requireContext(), R.color.indigo_ink))
                    lineData.addDataSet(dataSet)
                    if(lineData.dataSetCount>1){
                        lineData.removeDataSet(0)
                    }
                    lineChart.data = lineData
                    lineData.notifyDataChanged()
                    lineChart.notifyDataSetChanged()
                    lineChart.invalidate()
                }
            }


        })
}
}
