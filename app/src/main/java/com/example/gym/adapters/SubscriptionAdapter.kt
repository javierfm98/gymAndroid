package com.example.gym.adapters

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gym.R
import com.example.gym.inflate
import com.example.gym.models.Subscription
import kotlinx.android.synthetic.main.recycler_subscription.view.*


class SubscriptionAdapter (private val subscriptions: List<Subscription>): RecyclerView.Adapter<SubscriptionAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(parent.inflate(R.layout.recycler_subscription))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(subscriptions[position])

    override fun getItemCount(): Int = subscriptions.size




    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(subscription:  Subscription) = with(itemView){

            if(subscription.status == 0){
                texViewStatus.text = "SIN PAGAR"
                linearLayoutSubscription.setBackgroundColor(Color.parseColor("#cd5e54"))
            }else{
                texViewStatus.text = "PAGADO"
                linearLayoutSubscription.setBackgroundColor(Color.parseColor("#a1e197"))
            }

            textViewDaySubscription.text = subscription.end_at
        }

    }



}