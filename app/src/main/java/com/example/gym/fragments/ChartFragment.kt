package com.example.gym.fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gym.R
import com.example.gym.io.ApiService
import com.example.gym.io.response.ChartResponse
import com.example.gym.toast
import com.example.gym.utils.CustomMarkerView
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import kotlinx.android.synthetic.main.fragment_chart.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ChartFragment : Fragment() {

    private var axisDate: ArrayList<String> = arrayListOf()
    private var dataGoalWeight: ArrayList<Float> = arrayListOf()
    private var dataGoalBodyFat: ArrayList<Float> = arrayListOf()
    private var dataWeight: ArrayList<Float> = arrayListOf()
    private var dataBodyFat: ArrayList<Float> = arrayListOf()
    private var pointStartWeight: Int = -1
    private var pointStartBodyFat: Int = -1

    private  var jwt: String? = ""
    private lateinit var rootView: View

    private val apiService: ApiService by lazy {
        ApiService.create()
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
         rootView = inflater.inflate(R.layout.fragment_chart, container, false)

        val preferences = rootView.context.getSharedPreferences("general" , Context.MODE_PRIVATE)
        jwt = preferences.getString("session" , "")

        return rootView
    }

    override fun onResume() {
        super.onResume()
        getDataChart()
       // activity?.toast("Activo graficas")
    }

    private fun getDataChart() {
        val call = apiService.getDataChart("Bearer $jwt")
        call.enqueue(object: Callback<ChartResponse> {
            override fun onResponse(call: Call<ChartResponse>, response: Response<ChartResponse>) {
                if(response.isSuccessful){
                    val data = response.body()
                    data?.let {
                        axisDate = data.countMonths
                        dataGoalWeight = data.goalWeightCount
                        dataGoalBodyFat = data.goalBodyFatCount
                        dataWeight = data.arrayWeight
                        dataBodyFat = data.arrayBodyFat
                        pointStartWeight = data.pointStartWeight
                        pointStartBodyFat = data.pointStartBodyFat
                        initLineChart()
                        setDataToLineChart()
                    }
                }
            }

            override fun onFailure(call: Call<ChartResponse>, t: Throwable) {
                activity?.toast(t.localizedMessage)
            }

        })
    }

    private fun initLineChart() {
        //hide grid lines
        rootView.lineChart.axisLeft.setDrawGridLines(false)
        val xAxis: XAxis = rootView.lineChart.xAxis
        xAxis.setDrawGridLines(false)
        rootView.lineChart.axisLeft.axisMinimum = 0f

        //remove right y-axis
        rootView.lineChart.axisRight.isEnabled = false

        //remove description label
        rootView.lineChart.description.isEnabled = false

        rootView.lineChart.setExtraOffsets(5f,10f,5f,15f)

        //to draw label on xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.valueFormatter = MyAxisFormatter()
        xAxis.setDrawLabels(true)
        xAxis.granularity = 1f
        xAxis.labelRotationAngle = -75f
    }

    inner class MyAxisFormatter : IndexAxisValueFormatter() {

        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            val index = value.toInt()
            return if (index < axisDate.size) {
                axisDate[index]
            } else {
                ""
            }
        }
    }

    private fun setDataToLineChart() {
        //now draw bar chart with dynamic data
        val entriesGoalWeight: ArrayList<Entry> = ArrayList()
        val entriesGoalBodyFat: ArrayList<Entry> = ArrayList()
        val entriesWeight: ArrayList<Entry> = ArrayList()
        val entriesBodyFat: ArrayList<Entry> = ArrayList()

        addEntries(entriesGoalWeight, dataGoalWeight,0)
        addEntries(entriesGoalBodyFat, dataGoalBodyFat, 0)


        addEntries(entriesWeight, dataWeight , pointStartWeight)
        addEntries(entriesBodyFat, dataBodyFat, pointStartBodyFat)


        val lineDataSetGoalWeight = LineDataSet(entriesGoalWeight, "Objetivo Peso")
        val lineDataSetGoalBodyFat = LineDataSet(entriesGoalBodyFat, " Objetivo % Grasa")
        val lineDataSetWeight = LineDataSet(entriesWeight, "Peso")
        val lineDataSetBodyFat = LineDataSet(entriesBodyFat, "% Grasa")

        styleLineGoals(lineDataSetGoalWeight, Color.parseColor("#7cb5ec"))
        styleLineGoals(lineDataSetGoalBodyFat, Color.parseColor("#434348"))

        if(dataWeight.size > 1){
            styleLineBasic(lineDataSetWeight, Color.parseColor("#90ed7d"))
        }else{
            styleLineOnePoint(lineDataSetWeight, Color.parseColor("#90ed7d"))
        }

        if(dataBodyFat.size > 1){
            styleLineBasic(lineDataSetBodyFat, Color.parseColor("#f7a35c"))
        }else{
            styleLineOnePoint(lineDataSetBodyFat, Color.parseColor("#f7a35c"))
        }


        styleLineOnePoint(lineDataSetBodyFat, Color.MAGENTA)

        val dataSets: ArrayList<ILineDataSet> = ArrayList()
        dataSets.add(lineDataSetGoalWeight)
        dataSets.add(lineDataSetGoalBodyFat)
        dataSets.add(lineDataSetWeight)
        dataSets.add(lineDataSetBodyFat)

        val mv = CustomMarkerView(
            rootView.context,
            R.layout.marker_view
        )


        val data = LineData(dataSets)
        rootView.lineChart.data = data
        rootView.lineChart.setScaleEnabled(false)
        rootView.lineChart.setTouchEnabled(true)
        rootView.lineChart.marker = mv
        rootView.lineChart.invalidate()
    }


    private fun styleLineGoals(line: LineDataSet, color: Int){
        line.lineWidth = 3f
        line.color = color
        line.valueTextSize = 10f
        line.setDrawCircles(false)
        line.setDrawValues(false)
        line.enableDashedLine(5f, 10f, 0f)
    }

    private  fun styleLineBasic(line: LineDataSet, color: Int){
        line.lineWidth = 3f
        line.color = color
        line.valueTextSize = 10f
        line.setDrawValues(false)
        line.setDrawCircles(false)
    }

    private fun styleLineOnePoint(line: LineDataSet, color: Int){
        line.lineWidth = 3f
        line.color = color
        line.setDrawValues(false)
        line.circleHoleColor = color
        line.circleRadius = 2f
        line.setCircleColors(color)

    }

    private fun addEntries(entry: ArrayList<Entry> , data: ArrayList<Float>, pointStart: Int){
        for (i in data.indices){
            val value = data[i]
            entry.add(Entry((i+pointStart.toFloat()), value))
        }
    }




}
