package com.example.gym.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gym.R
import com.example.gym.activities.TrainingDetailsActivity
import com.example.gym.adapters.ReservationAdapter
import com.example.gym.adapters.TrainingAdapter
import com.example.gym.io.ApiService
import com.example.gym.listeners.RecyclerReservationListener
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
    private lateinit var contextView: Context

    private val apiService: ApiService by lazy {
        ApiService.create()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_reservation, container, false)

        recycler = rootView.recyclerViewReservation as RecyclerView
        contextView = rootView.context
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
                            textViewListReserves.text = "No tienes ning??n entreno cerca"
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

        adapter = (ReservationAdapter(reservations, object :RecyclerReservationListener{
            override fun onClick(training: Training, position: Int) {
             //   activity?.toast("Ver!! ${training.id}")
                val intent = Intent(contextView, TrainingDetailsActivity::class.java)
                intent.putExtra("training_id" , training.id)
                startActivity(intent)
            }
        }))

        recycler.adapter = adapter
    }
}


