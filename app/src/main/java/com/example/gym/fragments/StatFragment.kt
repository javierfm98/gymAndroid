package com.example.gym.fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gym.R
import com.example.gym.io.ApiService
import com.example.gym.models.Score
import com.example.gym.toast
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import kotlinx.android.synthetic.main.fragment_stat.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class StatFragment : Fragment() {

    lateinit var linelist: ArrayList<Entry>
    lateinit var lineDataSet: LineDataSet
    lateinit var lineData: LineData

    private var scoreList = ArrayList<Score>()
    private var scoreList2 = ArrayList<Score>()
    private var axisDate: ArrayList<String> = arrayListOf()

    private  var jwt: String? = ""

    private val apiService: ApiService by lazy {
        ApiService.create()
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_stat, container, false)

        val preferences = rootView.context.getSharedPreferences("general" , Context.MODE_PRIVATE)
        jwt = preferences.getString("session" , "")

        getDateAxis(rootView)


     /*   initLineChart(rootView)
        setDataToLineChart(rootView)*/

        return rootView
    }

    private fun getDateAxis(rootView: View) {
        val call = apiService.getAxis("Bearer $jwt")
        call.enqueue(object: Callback<ArrayList<String>> {
            override fun onResponse(call: Call<ArrayList<String>>, response: Response<ArrayList<String>>) {
                if(response.isSuccessful){
                    val axisVal = response.body()
                    axisVal?.let {
                        axisDate = axisVal
                        initLineChart(rootView)
                        setDataToLineChart(rootView)
                    }
                }else{
                    activity?.toast("Malll")
                }
            }

            override fun onFailure(call: Call<ArrayList<String>>, t: Throwable) {
                activity?.toast(t.localizedMessage)
            }

        })
    }

    private fun initLineChart(rootView: View) {
        // hide grid lines
        rootView.lineChart.axisLeft.setDrawGridLines(false)
        val xAxis: XAxis = rootView.lineChart.xAxis
        xAxis.setDrawGridLines(false)
      //  xAxis.setDrawAxisLine(false)

        rootView.lineChart.axisLeft.axisMinimum = 0f

        //remove right y-axis
        rootView.lineChart.axisRight.isEnabled = false

        //remove legend
       // rootView.lineChart.legend.isEnabled = false

        //remove description label
        rootView.lineChart.description.isEnabled = false


        //add animation
     //   rootView.lineChart.animateX(1000, Easing.EaseInSine)

        // to draw label on xAxis
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

    private fun setDataToLineChart(rootView: View) {
        //now draw bar chart with dynamic data
        val entries: ArrayList<Entry> = ArrayList()
        val entries2: ArrayList<Entry> = ArrayList()

        scoreList = getScoreList()
        scoreList2 = getScoreList2()

        //you can replace this data object with  your custom object
        for (i in scoreList.indices) {
            val score = scoreList[i]
            entries.add(Entry(i.toFloat(), score.score.toFloat()))
        }

        for (i in scoreList2.indices) {
            val score = scoreList2[i]
            entries2.add(Entry(i.toFloat()+1, score.score.toFloat()))
        }


        val lineDataSet = LineDataSet(entries, "Peso")
        val lineDataSet2 = LineDataSet(entries2, "% Grasa")

        lineDataSet.lineWidth = 3f
        lineDataSet.color = Color.BLUE
        lineDataSet.setDrawCircles(false)
    /*    lineDataSet.circleHoleColor = Color.BLUE
        lineDataSet.setCircleColors(Color.BLUE)*/
        lineDataSet.setDrawValues(false)
        lineDataSet.valueTextSize = 10f
        lineDataSet.enableDashedLine(8f, 10f, 10f)



        lineDataSet2.lineWidth = 3f
        //lineDataSet2.circleHoleRadius = 10f
        lineDataSet2.circleHoleColor = Color.RED
        lineDataSet2.setCircleColors(Color.RED)
        val dataSets: ArrayList<ILineDataSet> = ArrayList()
        dataSets.add(lineDataSet)
        dataSets.add(lineDataSet2)

        val data = LineData(dataSets)
        rootView.lineChart.data = data
        rootView.lineChart.setScaleEnabled(false)
        rootView.lineChart.invalidate()
    }

    // simulate api call
    // we are initialising it directly
    private fun getScoreList(): ArrayList<Score> {
        scoreList.add(Score("2021-08-21", 80))
        scoreList.add(Score("2021-08-22", 80))
        scoreList.add(Score("2021-08-23", 80))

        return scoreList
    }

    private fun getScoreList2(): ArrayList<Score> {
        scoreList2.add(Score("2021-08-22", 195))

        return scoreList2
    }




}




