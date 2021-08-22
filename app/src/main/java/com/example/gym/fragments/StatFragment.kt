package com.example.gym.fragments

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gym.R
import com.example.gym.models.Score
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import kotlinx.android.synthetic.main.fragment_stat.view.*


class StatFragment : Fragment() {

    lateinit var linelist: ArrayList<Entry>
    lateinit var lineDataSet: LineDataSet
    lateinit var lineData: LineData

    private var scoreList = ArrayList<Score>()
    private var scoreList2 = ArrayList<Score>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_stat, container, false)

        //setLineChart(rootView)
        initLineChart(rootView)
        setDataToLineChart(rootView)

        return rootView
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
            return if (index < scoreList.size) {
                scoreList[index].name
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

    private fun setLineChart(rootView: View) {

        linelist = ArrayList()
        linelist.add(Entry(2f,40f))
        linelist.add(Entry(2f,30f))
        linelist.add(Entry(3f,35f))


        lineDataSet = LineDataSet(linelist, "Count")
        lineDataSet.valueTextColor = Color.BLUE
        lineDataSet.valueTextSize = 13f

        val xAxis = rootView.lineChart.xAxis
        xAxis.setDrawGridLines(false)
        val xLabel = arrayOf("L" , "M" , "X")



        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.valueFormatter = (object: ValueFormatter(){
            override fun getFormattedValue(value: Float): String {
                val index = value.toInt()
                return if(index < xLabel.size){
                    xLabel[index]
                }else{
                    ""
                }
            }
        })

        lineData = LineData(lineDataSet)
        rootView.lineChart.data = lineData
        rootView.lineChart.axisRight.isEnabled = false
        rootView.lineChart.axisLeft.setDrawGridLines(false)
        rootView.lineChart.setScaleEnabled(false)
    }



}




