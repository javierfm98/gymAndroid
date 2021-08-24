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
import com.example.gym.adapters.StatAdapter
import com.example.gym.io.ApiService
import com.example.gym.listeners.RecyclerStatListener
import com.example.gym.models.Body
import com.example.gym.toast
import kotlinx.android.synthetic.main.fragment_list_stat.*
import kotlinx.android.synthetic.main.fragment_list_stat.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ListStatFragment : Fragment() {

    private lateinit var recycler: RecyclerView
    private lateinit var adapter: StatAdapter
    private val layoutManager by lazy { LinearLayoutManager(context) }

    private var jwt: String? = ""

    private val apiService: ApiService by lazy {
        ApiService.create()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.fragment_list_stat, container, false)

        val preferences = rootView.context.getSharedPreferences("general" , Context.MODE_PRIVATE)
         jwt = preferences.getString("session" , "")

        recycler = rootView.recyclerViewStat as RecyclerView

        getValues()

        // Inflate the layout for this fragment
        return rootView
    }

    private fun getValues() {

        val call = apiService.getBodyData("Bearer $jwt")
        call.enqueue(object: Callback<ArrayList<Body>> {
            override fun onResponse(call: Call<ArrayList<Body>>, response: Response<ArrayList<Body>>) {
                if(response.isSuccessful){
                    val values = response.body()
                    values?.let {
                        if(values.size > 0){
                            textViewStats.text = ""
                        }else{
                            textViewStats.text = "No tienes ninguna medición hecha"
                        }
                        setRecyclerView(values)
                    }
                }
            }

            override fun onFailure(call: Call<ArrayList<Body>>, t: Throwable) {
                activity?.toast(t.localizedMessage)
            }

        })
    }

    private fun setRecyclerView(values: ArrayList<Body>) {
        recycler.setHasFixedSize(true)
        recycler.layoutManager = layoutManager
        adapter = (StatAdapter(values, object :RecyclerStatListener{
            override fun onClickRemove(body: Body, position: Int) {
             //   activity?.toast("Eliminar!!")
                removeBody(body.id)
            }

        }))

        recycler.adapter = adapter
    }

    private fun removeBody(bodyId: Int) {
        val call = apiService.removeBody("Bearer $jwt", bodyId)
        call.enqueue(object: Callback<ArrayList<Body>> {
            override fun onResponse(call: Call<ArrayList<Body>>, response: Response<ArrayList<Body>>) {
                if(response.isSuccessful){
                    val values = response.body()
                    values?.let {
                        if(values.size > 0){
                            textViewStats.text = ""
                        }else{
                            textViewStats.text = "No tienes ninguna medición hecha"
                        }
                        activity?.toast("Dato eliminado")
                        setRecyclerView(values)
                    }
                }
            }

            override fun onFailure(call: Call<ArrayList<Body>>, t: Throwable) {
                activity?.toast(t.localizedMessage)
            }

        })
    }


}