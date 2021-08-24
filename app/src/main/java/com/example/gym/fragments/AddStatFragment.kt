package com.example.gym.fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.example.gym.R
import com.example.gym.io.ApiService
import com.example.gym.toast
import com.example.gym.utils.DatePickerFragment
import kotlinx.android.synthetic.main.fragment_add_stat.*
import kotlinx.android.synthetic.main.fragment_add_stat.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AddStatFragment : Fragment() {

    private lateinit var rootView: View

    private val apiService: ApiService by lazy {
        ApiService.create()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

         rootView = inflater.inflate(R.layout.fragment_add_stat, container, false)

        // Inflate the layout for this fragment
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       textInputDatePicker.setOnClickListener {
            showDatePickerDialog()
        }

        buttonBody.setOnClickListener {
            activity?.let { it1 -> hideKeyboard(it1) }
            activity?.window?.decorView?.clearFocus()
            saveDataBody()
        }
    }

    private fun saveDataBody() {
        val preferences = rootView.context.getSharedPreferences("general" , Context.MODE_PRIVATE)
        val jwt = preferences.getString("session" , "")
        val date = rootView.textInputDatePicker.text.toString()
        val weight = rootView.textInputWeight.text.toString()
        val bodyFat = rootView.textInputBodyFat.text.toString()

        val call = apiService.storeStats("Bearer $jwt", date, weight, bodyFat)
        call.enqueue(object: Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
               if(response.isSuccessful){
                   activity?.toast("Datos guardados")
                   rootView.textInputDatePicker.setText("")
                   rootView.textInputWeight.setText("")
                   rootView.textInputBodyFat.setText("")
               }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                activity?.toast(t.localizedMessage)
            }

        })

    }

    private fun showDatePickerDialog() {
       val datePicker = DatePickerFragment{
               year, month, day ->onDateSelected(year, month, day)
       }
        datePicker.show(activity?.supportFragmentManager!!, "datePicker")
    }

   private fun onDateSelected(year: Int, month: Int, day: Int){
       textInputDatePicker.setText("$year/$month/$day")
   }

    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        if (null != activity.currentFocus) imm.hideSoftInputFromWindow(
            activity.currentFocus!!
                .applicationWindowToken, 0
        )
    }
}