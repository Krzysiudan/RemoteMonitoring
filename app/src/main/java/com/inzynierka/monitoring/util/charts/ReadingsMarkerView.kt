package com.inzynierka.monitoring.util.charts

import android.content.Context
import android.graphics.Canvas
import android.util.Log
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.google.android.material.textview.MaterialTextView
import com.inzynierka.monitoring.R
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class ReadingsMarkerView(
    context: Context?,
    layoutResource: Int,
    private val referenceTimestamp: Long,
    private val chart: LineChart
) :
    MarkerView(context, layoutResource) {
    private val TAG = "MarkerView"
    private val tvContent: MaterialTextView = findViewById(R.id.marker_view_text_view_value)
    private val tvContentData: MaterialTextView = findViewById(R.id.marker_view_text_view_data)
    private val mDataFormat: DateFormat
    private val mDate: Date

    init {
        // this markerview only displays a textview
        mDataFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        mDate = Date()
        this.chartView = chart
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    override fun refreshContent(e: Entry, highlight: Highlight) {
        Log.d(TAG, "refreshContent: entry x: ${e.x} entry y: ${e.y}")
        val currentTimestamp = e.x.toInt() + referenceTimestamp
        Log.d(TAG, "refreshContent: currentTimestamp : $currentTimestamp")
        val markerTextValueData =  getTimedate(currentTimestamp)
        tvContent.text = e.y.toString() // set the entry-value as the display text
        tvContentData.text = markerTextValueData
        Log.d(TAG, "refreshContent: tvContent.text ${tvContent.text}")
        Log.d(TAG, "refreshContent: tvContentData.text ${tvContentData.text}")

    }

    fun getXOffset(xpos: Float): Int {
        // this will center the marker-view horizontally
        return -(width / 2)
    }

    fun getYOffset(ypos: Float): Int {
        // this will cause the marker-view to be above the selected value
        return -height
    }

    private fun getTimedate(timestamp: Long): String {
        return try {
            mDate.time = timestamp
            Log.d(TAG, "getTimedate: date ${mDataFormat.format(mDate)}")
            mDataFormat.format(mDate)
        } catch (ex: Exception) {
            "xx"
        }
    }

    override fun draw(canvas: Canvas?, posX: Float, posY: Float) {
        super.draw(canvas, posX, posY)
        getOffsetForDrawingAtPoint(posX, posY)
    }
}