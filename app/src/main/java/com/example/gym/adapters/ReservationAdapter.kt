package com.example.gym.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gym.R
import com.example.gym.inflate
import com.example.gym.listeners.RecyclerReservationListener
import com.example.gym.listeners.RecyclerTrainingListener
import com.example.gym.models.Training
import kotlinx.android.synthetic.main.recycler_reservation.view.*

class ReservationAdapter(private val trainings: ArrayList<Training>,  private  val listener: RecyclerReservationListener): RecyclerView.Adapter<ReservationAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(parent.inflate(R.layout.recycler_reservation))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(trainings[position], listener)

    override fun getItemCount(): Int = trainings.size




    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(training: Training, listener: RecyclerReservationListener ) = with(itemView){
            texViewHourReservation.text = training.trainingTime
            textViewDay.text = training.trainingDay

            setOnClickListener{
                listener.onClick(training,adapterPosition)
            }
        }

    }

}