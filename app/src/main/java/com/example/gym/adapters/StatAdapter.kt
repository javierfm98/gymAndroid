package com.example.gym.adapters

import android.text.Html
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gym.R
import com.example.gym.models.Body
import com.example.gym.inflate
import com.example.gym.listeners.RecyclerStatListener
import kotlinx.android.synthetic.main.recycler_stat.view.*


class StatAdapter(private val bodies: ArrayList<Body>, private val listener: RecyclerStatListener) : RecyclerView.Adapter<StatAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(parent.inflate(R.layout.recycler_stat))


    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(bodies[position], listener)

    override fun getItemCount(): Int = bodies.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(body: Body, listener: RecyclerStatListener) = with(itemView) {

            val dateString = "<b>Fecha:</b> ${body.dateBeautyFormat}"
            var valueString = ""

            if(body.statId == 1){
                valueString = "<b>Peso:</b> ${body.value}"
            }else{
                valueString = "<b>% Grasa corporal:</b> ${body.value}"
            }

            textViewDateStat.text = Html.fromHtml(dateString)
            texViewValueStat.text = Html.fromHtml(valueString)

            buttonDeleteStat.setOnClickListener {
                listener.onClickRemove(body, adapterPosition)
            }
        }
    }

}
