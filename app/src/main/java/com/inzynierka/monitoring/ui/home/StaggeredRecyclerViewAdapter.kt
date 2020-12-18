package com.inzynierka.monitoring.ui.home

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Point
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide.with
import com.inzynierka.monitoring.R
import com.inzynierka.monitoring.services.GlideApp
import com.inzynierka.monitoring.services.GlideApp.with

import com.inzynierka.monitoring.services.MyGlideApp

class StaggeredRecyclerViewAdapter internal constructor(
    private val context: Context,
) : RecyclerView.Adapter<StaggeredRecyclerViewAdapter.StaggeredCardViewHolder>() {

    private var TAG = "StaggeredRecyclerViewAdapter"
    private val sensorsList :TypedArray = context.resources.obtainTypedArray(R.array.sensors)
    private val sensorsImages :TypedArray = context.resources.obtainTypedArray(R.array.sensor_imgs)
    var onItemClick: ((String) -> Unit)? = null


    inner class StaggeredCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var sensorImage: ImageView = itemView.findViewById(R.id.sensor_image)
        var sensorTittle: TextView = itemView.findViewById(R.id.sensor_title)

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(sensorsList.getString(adapterPosition).toString())
            }

        }
    }

    override fun getItemViewType(position: Int): Int {
        return position % 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StaggeredCardViewHolder {
        Log.d(TAG, "onCreateViewHolder: ")
        var layoutId = R.layout.staggered_plot_view_card_first
        if (viewType == 1) {
            layoutId = R.layout.staggered_plot_view_card_second
        }
        val layoutView =
            LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return StaggeredCardViewHolder(layoutView)
    }

    override fun getItemCount(): Int {
        return sensorsList.length()
    }

    override fun onBindViewHolder(holder: StaggeredCardViewHolder, position: Int) {
        val currentSensor: String = sensorsList.getString(position).toString()
        Log.d(TAG, "Function onBindViewHolder: currentSensor - $currentSensor")
        holder.sensorTittle.text = currentSensor
        GlideApp.with(context).load(sensorsImages.getResourceId(position,0)).into(holder.sensorImage)
    }

}


