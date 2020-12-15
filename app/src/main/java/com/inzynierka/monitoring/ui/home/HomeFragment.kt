package com.inzynierka.monitoring.ui.home

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.inzynierka.monitoring.R

/**
 * An example full-screen fragment that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class HomeFragment : Fragment() {
    private val TAG = "HomeFragment"

    private lateinit var homeViewModel: HomeViewModel



    private lateinit var recyclerViewAdapter: StaggeredRecyclerViewAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView: ")
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        setupRecyclerView(root)
        return root
    }

    private fun setupRecyclerView(root: View) {
        Log.d(TAG, "setupRecyclerView: ")
        val recyclerView = root.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerViewAdapter =
            StaggeredRecyclerViewAdapter(activity as Context)

        recyclerView.setHasFixedSize(true)
        val gridLayoutManager = GridLayoutManager(context, 2, RecyclerView.VERTICAL, false)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position % 3 == 2) 2 else 1
            }
        }
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.addItemDecoration(
            GridItemDecoration(
                resources.getDimensionPixelSize(R.dimen.sensor_grid_spacing),
                resources.getDimensionPixelSize(R.dimen.sensor_grid_spacing_small)
            )
        )
        setupOnItemClick(recyclerViewAdapter)
        Log.d(TAG, "setupRecyclerView: item count : ${recyclerViewAdapter.itemCount}")
    }

    private fun setupOnItemClick(adapter: StaggeredRecyclerViewAdapter) {

        recyclerViewAdapter.onItemClick = { sensorName ->

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)


    }
    
}