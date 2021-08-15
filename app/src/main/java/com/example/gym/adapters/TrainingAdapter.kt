package com.example.gym.adapters

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gym.R
import com.example.gym.inflate
import com.example.gym.io.ApiService
import com.example.gym.io.response.CheckResponse
import com.example.gym.listeners.RecyclerTrainingListener
import com.example.gym.models.Training
import kotlinx.android.synthetic.main.recycler_training.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList

class TrainingAdapter(private val trainings: ArrayList<Training>, private  val listener: RecyclerTrainingListener): RecyclerView.Adapter<TrainingAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(parent.inflate(R.layout.recycler_training))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(trainings[position], listener)

    override fun getItemCount(): Int = trainings.size




    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        private val apiService: ApiService by lazy {
            ApiService.create()
        }

        private var checking: Training? = null
        private var cosa: String? = "Ini"

        fun bind(training: Training , listener: RecyclerTrainingListener) = with(itemView){
            val totalClient = training.enroll.toString() + "/" + training.capacity.toString()
            textViewHour.text = training.trainingTime
            textViewEnroll.text = totalClient
            val preferences = context.getSharedPreferences("general" , Context.MODE_PRIVATE)
            val jwt = preferences.getString("session" , "")!!

            checkReservations(training.trainingDayDB,jwt,buttonReservation,training)

            //Clicks Events
            setOnClickListener {
                listener.onClick(training,adapterPosition)
            }
            buttonReservation.setOnClickListener {
                if(buttonReservation.text == "RESERVADO"){
                    listener.onUnsubscribe(training,adapterPosition)
                }else{
                    listener.onReservation(training,adapterPosition)
                }

            }
        }

        private fun checkReservations(date: String, jwt: String, buttonReservation: Button , training: Training) {
            val call = apiService.checkReservation("Bearer $jwt" , date)
            call.enqueue(object: Callback<CheckResponse> {
                override fun onResponse(call: Call<CheckResponse>, response: Response<CheckResponse>) {
                    if(response.isSuccessful){
                        val checkResponse = response.body()
                        checkResponse?.let {
                            if(checkResponse.success){
                               prueba(checkResponse.training,buttonReservation,training)
                            }else{
                               // prueba(textViewEnroll)
                            }
                        }
                    }
                }


                override fun onFailure(call: Call<CheckResponse>, t: Throwable) {
                    // toast(t.localizedMessage)
                }

            } )
        }

         fun prueba(trainingCheck: Training , buttonReservation: Button,training: Training) {
             if(training.id == trainingCheck.id){
                 buttonReservation.text = "RESERVADO"
                 buttonReservation.setBackgroundColor(Color.rgb(0, 255, 0))
               /*  buttonReservation.isEnabled = false
                 buttonReservation.isClickable = false*/
             }else{
                 buttonReservation.isEnabled = false
                 buttonReservation.isClickable = false
             }

        }


    }




}