package com.example.gym.fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gym.R
import com.example.gym.adapters.ViewPagerAdapter
import com.example.gym.io.ApiService
import com.example.gym.toast
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_stat.*
import kotlinx.android.synthetic.main.fragment_stat.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class StatFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureTabLayout()
    }

    private fun configureTabLayout() {
        viewPager.adapter = ViewPagerAdapter(childFragmentManager, lifecycle)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Gráfica"
                1 -> tab.text = "Añadir"
                2 -> tab.text = "Objetivos"
                3 -> tab.text = "Mediciones"
            }
        }.attach()
    }
}
