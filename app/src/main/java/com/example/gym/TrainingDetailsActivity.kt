package com.example.gym

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gym.adapters.ReservationAdapter
import com.example.gym.adapters.UserAdapter
import com.example.gym.io.ApiService
import com.example.gym.io.response.TrainingDetailsResponse
import com.example.gym.models.User
import kotlinx.android.synthetic.main.activity_training_details.*
import kotlinx.android.synthetic.main.fragment_trainings.*
import kotlinx.android.synthetic.main.fragment_trainings.view.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TrainingDetailsActivity : AppCompatActivity() {

    private val apiService: ApiService by lazy {
        ApiService.create()
    }

    private lateinit var recycler: RecyclerView
    private lateinit var adapter: UserAdapter
   // private val layoutManager by lazy { LinearLayoutManager(this) }
    private lateinit var layoutManager: LinearLayoutManager



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_training_details)

        setSupportActionBar(toolbar)
        setTitle("Detalles del entreno")

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        layoutManager = LinearLayoutManager(this)
        recycler = recyclerViewTrainingDetails as RecyclerView

        val trainingId: Int = intent.getIntExtra("training_id" , -1)
        getInfo(trainingId)

    }


    private fun getInfo(trainingId: Int) {
        val preferences = getSharedPreferences("general" , Context.MODE_PRIVATE)
        val jwt = preferences.getString("session" , "")
        val call = apiService.showTraining("Bearer $jwt",trainingId)
        call.enqueue(object: Callback<TrainingDetailsResponse> {
            override fun onResponse(call: Call<TrainingDetailsResponse>, response: Response<TrainingDetailsResponse>) {
                if(response.isSuccessful){
                    val users = response.body()?.allUsers
                    users?.let {
                        setRecyclerView(users)
                    }
                }
            }

            override fun onFailure(call: Call<TrainingDetailsResponse>, t: Throwable) {
                toast(t.localizedMessage)
            }

        })
    }

    private fun setRecyclerView(users: ArrayList<User>) {
        recycler.setHasFixedSize(true)
        recycler.layoutManager = layoutManager
        adapter = (UserAdapter(users))

        recycler.adapter = adapter
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}