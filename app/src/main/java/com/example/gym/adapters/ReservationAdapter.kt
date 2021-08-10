package com.example.gym.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gym.R
import com.example.gym.inflate
import com.example.gym.models.Training
import kotlinx.android.synthetic.main.recycler_reservation.view.*

class ReservationAdapter(private val trainings: List<Training>): RecyclerView.Adapter<ReservationAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(parent.inflate(R.layout.recycler_reservation))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(trainings[position])

    override fun getItemCount(): Int = trainings.size




    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(training: Training ) = with(itemView){
            texViewHourReservation.text = training.timeFormat
            textViewDay.text = training.day
        }

    }

}