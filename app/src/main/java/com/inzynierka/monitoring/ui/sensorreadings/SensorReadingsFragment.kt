package com.inzynierka.monitoring.ui.sensorreadings

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.inzynierka.monitoring.R
import com.inzynierka.monitoring.util.SubscribeEvent
import com.inzynierka.monitoring.util.charts.AxisValueFormatter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*

@ExperimentalCoroutinesApi
class SensorReadingsFragment : Fragment(), SubscribeEvent {

    private val TAG = "SensorReadingFragment"
    private val args: SensorReadingsFragmentArgs by navArgs()
    private val sensorReadingsViewModel: SensorReadingsViewModel by viewModels { SensorReadingsViewModelFactory(args.sensorName) }
    private lateinit var sensorNameInLocale : String
    private lateinit var lineChart: LineChart
    private lateinit var lineData: LineData
    private lateinit var dataSet: LineDataSet

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_sensor_readings, container, false)
        lineChart = root.findViewById<LineChart>(R.id.graph)
        lineData = LineData()
        sensorReadingsViewModel.subscribeEvent.setEventReceiver(this,this)
        sensorNameInLocale = getSensorNameInLocale(args.sensorName)


        return root
    }


    override fun subscribeLiveData(referenceTimeStamp: Long) {
        val xAxis = lineChart.xAxis
        xAxis.valueFormatter = AxisValueFormatter(referenceTimeStamp)
        sensorReadingsViewModel.sensorReadings.observe(viewLifecycleOwner, Observer {

            if (it != null) {
                if (it.isNotEmpty()) {
                    dataSet = LineDataSet(it, sensorNameInLocale)
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

    private fun getSensorNameInLocale(sensorName: String): String{
        var transformedName = sensorName.toLowerCase(Locale.getDefault())
        Log.d(TAG, "getSensorNameInLocale: transformedName: $transformedName")
        transformedName = transformedName.replace(" ","_")
        Log.d(TAG, "getSensorNameInLocale: transformedName after replace: $transformedName")
        val nameId: Int = resources.getIdentifier(transformedName,"string",requireContext().packageName)
        Log.d(TAG, "getSensorNameInLocale: nameId: $nameId")
        var sensorNameInLocale = sensorName
        if(nameId !=0){
            sensorNameInLocale = resources.getString(nameId)
        }
        Log.d(TAG, "getSensorNameInLocale: sensorNameInLocale: $sensorNameInLocale")
        return sensorNameInLocale
    }
}
