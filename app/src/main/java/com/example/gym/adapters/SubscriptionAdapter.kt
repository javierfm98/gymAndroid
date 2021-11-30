package com.example.gym.adapters

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gym.R
import com.example.gym.inflate
import com.example.gym.models.Subscription
import kotlinx.android.synthetic.main.recycler_subscription.view.*


class SubscriptionAdapter (private val subscriptions: ArrayList<Subscription>): RecyclerView.Adapter<SubscriptionAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(parent.inflate(R.layout.recycler_subscription))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(subscriptions[position])

    override fun getItemCount(): Int = subscriptions.size




    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(subscription:  Subscription) = with(itemView){

            if(subscription.status == 0){
                textViewStatus.text = "SIN PAGAR"
                textViewStatus.setBackgroundResource(R.drawable.border_radius_subscriptions_red)
            }else if(subscription.status == 1){
                textViewStatus.text = "PAGADO"
                textViewStatus.setBackgroundResource(R.drawable.border_radius_subscriptions_green)
            }else{
                textViewStatus.text = "PENDIENTE"
                textViewStatus.setBackgroundResource(R.drawable.border_radius_subscriptions_yellow)
            }

            textViewDaySubscription.text = subscription.subsEnd
        }

    }
}