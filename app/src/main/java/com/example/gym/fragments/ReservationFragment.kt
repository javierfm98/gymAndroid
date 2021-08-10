package com.example.gym.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gym.R
import com.example.gym.adapters.ReservationAdapter
import com.example.gym.adapters.TrainingAdapter
import com.example.gym.listeners.RecyclerTrainingListener
import com.example.gym.models.Training
import com.example.gym.toast
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_navigation.*
import kotlinx.android.synthetic.main.fragment_reservation.view.*
import kotlinx.android.synthetic.main.fragment_trainings.view.*


class ReservationFragment : Fragment() {



    private lateinit var recycler: RecyclerView
    private lateinit var adapter: ReservationAdapter
    private val layoutManager by lazy { LinearLayoutManager(context) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        activity?.setTitle("Mis reservas")

        val navigationView = activity?.nav_view as NavigationView
        navigationView.setCheckedItem(R.id.nav_reservation)

        val rootView = inflater.inflate(R.layout.fragment_reservation, container, false)

        recycler = rootView.recyclerViewReservation as RecyclerView
        setRecyclerView()



        return rootView
    }

    private fun setRecyclerView() {
        recycler.setHasFixedSize(true)
        recycler.layoutManager = layoutManager
        //adapter = (ReservationAdapter(list))

        recycler.adapter = adapter
    }




}