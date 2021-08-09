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
import com.example.gym.adapters.SubscriptionAdapter
import com.example.gym.models.Subscription
import com.example.gym.models.Training
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_navigation.*
import kotlinx.android.synthetic.main.fragment_subscription.view.*


class SubscriptionFragment : Fragment() {

    private  val list: ArrayList<Subscription> by lazy { getSubscriptions() }

    private lateinit var recycler: RecyclerView
    private lateinit var adapter: SubscriptionAdapter
    private val layoutManager by lazy { LinearLayoutManager(context) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        activity?.setTitle("Mis Pagos")

        val navigationView = activity?.nav_view as NavigationView
        navigationView.setCheckedItem(R.id.nav_subscription)

        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_subscription, container, false)

        recycler = rootView.recyclerViewSubscription as RecyclerView
        setRecyclerView()

        return rootView
    }

    private fun setRecyclerView() {
        recycler.setHasFixedSize(true)
        recycler.layoutManager = layoutManager
        adapter = (SubscriptionAdapter(list))

        recycler.adapter = adapter
    }

    private fun getSubscriptions(): ArrayList<Subscription>{
        return object: ArrayList<Subscription>(){
            init{
                add(Subscription(1,1,1,"20/06/2021"))
                add(Subscription(1,1,1,"20/07/2021"))
                add(Subscription(1,1,0,"20/08/2021"))
            }
        }
    }

}