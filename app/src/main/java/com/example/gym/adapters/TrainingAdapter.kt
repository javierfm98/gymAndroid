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
          /*  val totalClient = training.enroll.toString() + "/" + training.capacity.toString()
            textViewHour.text = training.trainingTime
            textViewEnroll.text = totalClient
            itemView.buttonReservation.text = "RESERVAR"*/
            val preferences = context.getSharedPreferences("general" , Context.MODE_PRIVATE)
            val jwt = preferences.getString("session" , "")!!

            checkReservations(training.trainingDayDB,jwt,itemView,training,listener)

            //Clicks Events
            setOnClickListener {
                listener.onClick(training,adapterPosition)
            }
        }

        private fun checkReservations(date: String, jwt: String, itemView: View , training: Training,listener: RecyclerTrainingListener) {
            val call = apiService.checkReservation("Bearer $jwt" , date)
            call.enqueue(object: Callback<CheckResponse> {
                override fun onResponse(call: Call<CheckResponse>, response: Response<CheckResponse>) {
                    if(response.isSuccessful){
                        val checkResponse = response.body()
                        checkResponse?.let {
                            if(checkResponse.success){
                               updateTraining(checkResponse.training,itemView,training,listener)
                            }else{
                               setItemsView(itemView,training,listener)
                            }
                        }
                    }
                }


                override fun onFailure(call: Call<CheckResponse>, t: Throwable) {
                    // toast(t.localizedMessage)
                }

            } )
        }

         fun updateTraining(trainingCheck: Training , itemView: View,training: Training,listener: RecyclerTrainingListener) {
             if(training.id == trainingCheck.id){

                 val totalClient = trainingCheck.enroll.toString() + "/" + training.capacity.toString()
                 itemView.buttonReservation.text = "RESERVADO"
                 itemView.buttonReservation.setBackgroundColor(Color.rgb(0, 255, 0))
                 itemView.textViewHour.text = training.trainingTime
                 itemView.textViewEnroll.text = totalClient

                 itemView.buttonReservation.setOnClickListener {
                     listener.onRemoveReservation(training,adapterPosition)
                 }


             }else{
                 val totalClient = training.enroll.toString() + "/" + training.capacity.toString()
                 itemView.textViewHour.text = training.trainingTime
                 itemView.textViewEnroll.text = totalClient
                 itemView.buttonReservation.text = "RESERVAR"
                 itemView.buttonReservation.setBackgroundColor(Color.rgb(0, 184, 255))

                 itemView.buttonReservation.setOnClickListener {
                     listener.onNoMoreReservation(training,adapterPosition)
                 }

             }

        }

        fun setItemsView(itemView: View,training: Training,listener: RecyclerTrainingListener){
            val totalClient = training.enroll.toString() + "/" + training.capacity.toString()
            itemView.textViewHour.text = training.trainingTime
            itemView.textViewEnroll.text = totalClient
            itemView.buttonReservation.text = "RESERVAR"
            itemView.buttonReservation.setBackgroundColor(Color.rgb(0, 184, 255))
            itemView.buttonReservation.isEnabled = true
            itemView.buttonReservation.isClickable = true

            itemView.buttonReservation.setOnClickListener {
                listener.onReservation(training,adapterPosition)
            }

        }


    }




}