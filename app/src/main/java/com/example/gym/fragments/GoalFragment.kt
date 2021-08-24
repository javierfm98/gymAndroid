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
import com.example.gym.activities.NavigationActivity
import com.example.gym.io.ApiService
import com.example.gym.io.response.GoalResponse
import com.example.gym.toast
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.fragment_goal.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GoalFragment : Fragment() {

    private  var jwt: String? = ""
    private  lateinit var rootView: View

    private val apiService: ApiService by lazy {
        ApiService.create()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

         rootView = inflater.inflate(R.layout.fragment_goal, container, false)

        val preferences = rootView.context.getSharedPreferences("general" , Context.MODE_PRIVATE)
        jwt = preferences.getString("session" , "")

        getDataGoals()

        rootView.buttonGoalStore.setOnClickListener {
            val weight = rootView.textInputGoalWeight.text.toString()
            val bodyFat = rootView.textInputGoalBodyFat.text.toString()

            activity?.let { it1 -> hideKeyboard(it1) }
            storeGoal(weight, bodyFat)
        }

        // Inflate the layout for this fragment
        return rootView
    }

    private fun getDataGoals() {
        val call = apiService.getGoal("Bearer $jwt")
        call.enqueue(object: Callback<GoalResponse> {
            override fun onResponse(call: Call<GoalResponse>, response: Response<GoalResponse>) {
                if(response.isSuccessful){
                    val goals = response.body()
                    goals?.let {
                        rootView.textInputGoalWeight.setText(goals.goalsWeight.toString())
                        rootView.textInputGoalBodyFat.setText(goals.goalsBodyFat.toString())
                    }
                }
            }

            override fun onFailure(call: Call<GoalResponse>, t: Throwable) {
                activity?.toast(t.localizedMessage)
            }

        })
    }

    private fun storeGoal(weight: String, bodyFat: String) {
        val call = apiService.storeGoal("Bearer $jwt", weight, bodyFat)
        call.enqueue(object: Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if(response.isSuccessful){
                    activity?.toast("Objetivos guardados")
                    getDataGoals()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                activity?.toast(t.localizedMessage)
            }

        })
    }

    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        if (null != activity.currentFocus) imm.hideSoftInputFromWindow(
            activity.currentFocus!!
                .applicationWindowToken, 0
        )
    }

}