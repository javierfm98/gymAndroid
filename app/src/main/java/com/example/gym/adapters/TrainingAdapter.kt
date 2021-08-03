package com.example.gym.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gym.R
import com.example.gym.inflate
import com.example.gym.listeners.RecyclerTrainingListener
import com.example.gym.models.Training
import kotlinx.android.synthetic.main.recycler_training.view.*

class TrainingAdapter(private val trainings: List<Training>, private  val listener: RecyclerTrainingListener): RecyclerView.Adapter<TrainingAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(parent.inflate(R.layout.recycler_training))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(trainings[position], listener)

    override fun getItemCount(): Int = trainings.size




    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(training: Training , listener: RecyclerTrainingListener) = with(itemView){
            val finalHour = training.start + " - " + training.end
            val totalClient = training.enroll.toString() + "/" + training.capacity.toString()
            textViewHour.text = finalHour
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