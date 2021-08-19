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
import com.example.gym.adapters.SubscriptionAdapter
import com.example.gym.io.ApiService
import com.example.gym.models.Subscription
import com.example.gym.toast
import kotlinx.android.synthetic.main.fragment_reservation.*
import kotlinx.android.synthetic.main.fragment_reservation.textViewListReserves
import kotlinx.android.synthetic.main.fragment_subscription.*
import kotlinx.android.synthetic.main.fragment_subscription.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SubscriptionFragment : Fragment() {

    private lateinit var recycler: RecyclerView
    private lateinit var adapter: SubscriptionAdapter
    private val layoutManager by lazy { LinearLayoutManager(context) }

    private val apiService: ApiService by lazy {
        ApiService.create()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {


        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_subscription, container, false)

        recycler = rootView.recyclerViewSubscription as RecyclerView

        getSubscriptions(rootView.context)

        return rootView
    }

    private fun getSubscriptions(context: Context) {
        val preferences = context.getSharedPreferences("general" , Context.MODE_PRIVATE)
        val jwt = preferences.getString("session" , "")
        val call = apiService.getSubs("Bearer $jwt")
        call.enqueue(object: Callback<ArrayList<Subscription>> {
            override fun onResponse(call: Call<ArrayList<Subscription>>, response: Response<ArrayList<Subscription>>) {
                if(response.isSuccessful){
                    val subs = response.body()
                    subs?.let {
                        if(subs.size > 0){
                            textViewSubs.text = ""
                        }else{
                            textViewSubs.text = "No tienes ningún pago"
                        }
                        setRecyclerView(subs)
                    }
                }
            }

            override fun onFailure(call: Call<ArrayList<Subscription>>, t: Throwable) {
                activity?.toast(t.localizedMessage)
            }

        })
    }

    private fun setRecyclerView(subscription: ArrayList<Subscription>) {
        recycler.setHasFixedSize(true)
        recycler.layoutManager = layoutManager
        adapter = (SubscriptionAdapter(subscription))

        recycler.adapter = adapter
    }



}