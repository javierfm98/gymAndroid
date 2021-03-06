package com.example.gym.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gym.R
import com.example.gym.inflate
import com.example.gym.models.User
import com.example.gym.utils.CircleTransform
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.nav_header.view.*
import kotlinx.android.synthetic.main.recycler_user.view.*

class UserAdapter (private val users: ArrayList<User>): RecyclerView.Adapter<UserAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(parent.inflate(R.layout.recycler_user))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(users[position])

    override fun getItemCount(): Int = users.size

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(user: User) = with(itemView){
            val fullName = user.name + " " + user.surname
            val urlPhoto = "http://90.69.39.12/gym/public/img/"+user.photo.route

            textViewFullName.text = fullName

            Picasso.get()
                .load(urlPhoto)
                .fit()
                .transform(CircleTransform())
                .into(imageViewProfileTraining)
        }
    }

}