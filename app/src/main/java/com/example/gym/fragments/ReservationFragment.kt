package com.example.gym.fragments

import android.content.Context
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
import com.example.gym.io.ApiService
import com.example.gym.listeners.RecyclerTrainingListener
import com.example.gym.models.Training
import com.example.gym.toast
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_navigation.*
import kotlinx.android.synthetic.main.fragment_reservation.*
import kotlinx.android.synthetic.main.fragment_reservation.view.*
import kotlinx.android.synthetic.main.fragment_trainings.*
import kotlinx.android.synthetic.main.fragment_trainings.textViewEmpty
import kotlinx.android.synthetic.main.fragment_trainings.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ReservationFragment : Fragment() {



    private lateinit var recycler: RecyclerView
    private lateinit var adapter: ReservationAdapter
    private val layoutManager by lazy { LinearLayoutManager(context) }

    private val apiService: ApiService by lazy {
        ApiService.create()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        activity?.setTitle("Mis reservas")

        val navigationView = activity?.nav_view as NavigationView
        navigationView.setCheckedItem(R.id.nav_reservation)

        val rootView = inflater.inflate(R.layout.fragment_reservation, container, false)

        recycler = rootView.recyclerViewReservation as RecyclerView
        getReservation(rootView.context)


        return rootView
    }

    private fun getReservation(context: Context) {
        val preferences = context.getSharedPreferences("general" , Context.MODE_PRIVATE)
        val jwt = preferences.getString("session" , "")
        val call = apiService.getReserves("Bearer $jwt")
        call.enqueue(object: Callback<ArrayList<Training>> {
            override fun onResponse(call: Call<ArrayList<Training>>,response: Response<ArrayList<Training>>) {
                if(response.isSuccessful){
                    val reservations = response.body()
                    reservations?.let {
                        if(reservations.size > 0){
                            textViewListReserves.text = ""
                        }else{
                            textViewListReserves.text = "No estás apuntado a ningún entreno"
                        }
                        setRecyclerView(reservations)
                    }
                }
            }

            override fun onFailure(call: Call<ArrayList<Training>>, t: Throwable) {
                activity?.toast(t.localizedMessage)
            }

        })
    }

    private fun setRecyclerView(reservations: ArrayList<Training>) {
        recycler.setHasFixedSize(true)
        recycler.layoutManager = layoutManager
        adapter = (ReservationAdapter(reservations))

        recycler.adapter = adapter
    }




}