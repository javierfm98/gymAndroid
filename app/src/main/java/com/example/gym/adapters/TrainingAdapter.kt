package com.example.gym.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gym.R
import com.example.gym.inflate
import com.example.gym.listeners.RecyclerTrainingListener
import com.example.gym.models.Training
import kotlinx.android.synthetic.main.recycler_training.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TrainingAdapter(private val trainings: ArrayList<Training>, private  val listener: RecyclerTrainingListener): RecyclerView.Adapter<TrainingAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(parent.inflate(R.layout.recycler_training))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(trainings[position], listener)

    override fun getItemCount(): Int = trainings.size




    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(training: Training , listener: RecyclerTrainingListener) = with(itemView){
            val totalClient = training.enroll.toString() + "/" + training.capacity.toString()
            textViewHour.text = training.timeFormat
            textViewEnroll.text = totalClient

            //Clicks Events
            setOnClickListener {
                listener.onClick(training,adapterPosition)
            }
            buttonReservation.setOnClickListener {
                listener.onReservation(training,adapterPosition)
            }
        }

    }


}