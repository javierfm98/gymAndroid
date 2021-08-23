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

    private var axisDate: ArrayList<String> = arrayListOf()
    private var dataGoalWeight: ArrayList<Int> = arrayListOf()
    private var dataGoalBodyFat: ArrayList<Int> = arrayListOf()
    private var dataWeight: ArrayList<Int> = arrayListOf()
    private var dataBodyFat: ArrayList<Int> = arrayListOf()

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
                        getGoalWeight(rootView)

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

    private fun getGoalWeight(rootView: View) {
        val call = apiService.getGoalWeight("Bearer $jwt")
        call.enqueue(object: Callback<ArrayList<Int>> {
            override fun onResponse(call: Call<ArrayList<Int>>, response: Response<ArrayList<Int>>) {
                if(response.isSuccessful){
                    val weightValues = response.body()
                    weightValues?.let {
                        dataGoalWeight = weightValues
                        getGoalBodyFat(rootView)

                    }
                }
            }

            override fun onFailure(call: Call<ArrayList<Int>>, t: Throwable) {
                activity?.toast(t.localizedMessage)
            }

        })
    }

    private fun getGoalBodyFat(rootView: View) {
        val call = apiService.getGoalBodyFat("Bearer $jwt")
        call.enqueue(object: Callback<ArrayList<Int>> {
            override fun onResponse(call: Call<ArrayList<Int>>, response: Response<ArrayList<Int>>) {
                if(response.isSuccessful){
                    val bodyFatValues = response.body()
                    bodyFatValues?.let {
                        dataGoalBodyFat = bodyFatValues
                        getWeightData(rootView)

                    }
                }
            }

            override fun onFailure(call: Call<ArrayList<Int>>, t: Throwable) {
                activity?.toast(t.localizedMessage)
            }

        })
    }

    private fun getWeightData(rootView: View) {
        val call = apiService.getWeightData("Bearer $jwt")
        call.enqueue(object: Callback<ArrayList<Int>> {
            override fun onResponse(call: Call<ArrayList<Int>>, response: Response<ArrayList<Int>>) {
                if(response.isSuccessful){
                    val weightData = response.body()
                    weightData?.let {
                        dataWeight = weightData
                        getBodyFatData(rootView)
                    }
                }
            }

            override fun onFailure(call: Call<ArrayList<Int>>, t: Throwable) {
                activity?.toast(t.localizedMessage)
            }

        })
    }

    private  fun getBodyFatData(rootView: View){
        val call = apiService.getBodyFatData("Bearer $jwt")
        call.enqueue(object: Callback<ArrayList<Int>> {
            override fun onResponse(call: Call<ArrayList<Int>>, response: Response<ArrayList<Int>>) {
                if(response.isSuccessful){
                    val bodyFatData = response.body()
                    bodyFatData?.let {
                        dataBodyFat = bodyFatData
                        setDataToLineChart(rootView)
                    }
                }
            }

            override fun onFailure(call: Call<ArrayList<Int>>, t: Throwable) {
                activity?.toast(t.localizedMessage)
            }

        })
    }

    private fun initLineChart(rootView: View) {
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

    private fun setDataToLineChart(rootView: View) {
        //now draw bar chart with dynamic data
        val entriesGoalWeight: ArrayList<Entry> = ArrayList()
        val entriesGoalBodyFat: ArrayList<Entry> = ArrayList()
        val entriesWeight: ArrayList<Entry> = ArrayList()
        val entriesBodyFat: ArrayList<Entry> = ArrayList()


        for (i in dataGoalWeight.indices){
            val value = dataGoalWeight[i]
            entriesGoalWeight.add(Entry((i.toFloat()), value.toFloat()))
        }

        for(i in dataGoalBodyFat.indices){
            val value = dataGoalBodyFat[i]
            entriesGoalBodyFat.add(Entry((i.toFloat()), value.toFloat()))
        }

        for(i in dataWeight.indices){
            val value = dataWeight[i]
            entriesWeight.add(Entry((i.toFloat()), value.toFloat()))
        }

        for(i in dataBodyFat.indices){
            val value = dataBodyFat[i]
            entriesBodyFat.add(Entry((i+2.toFloat()), value.toFloat()))
        }



        val lineDataSetGoalWeight = LineDataSet(entriesGoalWeight, "Objetivo Peso")
        val lineDataSetGoalBodyFat = LineDataSet(entriesGoalBodyFat, " Objetivo % Grasa")
        val lineDataSetWeight = LineDataSet(entriesWeight, "Peso")
        val lineDataSetBodyFat = LineDataSet(entriesBodyFat, "% Grasa")

        styleLineGoals(lineDataSetGoalWeight, Color.BLUE)
        styleLineGoals(lineDataSetGoalBodyFat, Color.RED)

        styleLineBasic(lineDataSetWeight, Color.GREEN)
        styleLineBasic(lineDataSetBodyFat, Color.MAGENTA)

        val dataSets: ArrayList<ILineDataSet> = ArrayList()
        dataSets.add(lineDataSetGoalWeight)
        dataSets.add(lineDataSetGoalBodyFat)
        dataSets.add(lineDataSetWeight)
        dataSets.add(lineDataSetBodyFat)

        val data = LineData(dataSets)
        rootView.lineChart.data = data
        rootView.lineChart.setScaleEnabled(false)
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
       // line.setDrawValues(false)
     //   line.setDrawCircles(false)
    }




}




