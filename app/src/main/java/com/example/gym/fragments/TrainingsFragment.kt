package com.example.gym.fragments



import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gym.R
import com.example.gym.adapters.TrainingAdapter
import com.example.gym.io.ApiService
import com.example.gym.io.response.CheckResponse
import com.example.gym.listeners.RecyclerTrainingListener
import com.example.gym.models.Training
import com.example.gym.toast
import com.google.android.material.navigation.NavigationView
import devs.mulham.horizontalcalendar.HorizontalCalendar
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener
import kotlinx.android.synthetic.main.activity_navigation.*
import kotlinx.android.synthetic.main.fragment_trainings.*
import kotlinx.android.synthetic.main.fragment_trainings.view.*
import kotlinx.android.synthetic.main.recycler_training.*
import kotlinx.android.synthetic.main.recycler_training.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


class TrainingsFragment : Fragment() {


    private lateinit var recycler: RecyclerView
    private lateinit var adapter: TrainingAdapter
    private val layoutManager by lazy { LinearLayoutManager(context) }
    private  var jwt: String? = ""


    private val apiService: ApiService by lazy {
        ApiService.create()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {


        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_trainings, container, false)

        recycler = rootView.recyclerView as RecyclerView

        val preferences = rootView.context.getSharedPreferences("general" , Context.MODE_PRIVATE)
        jwt = preferences.getString("session" , "")

        // Calendar horizontal
        val date = System.currentTimeMillis()
        val formatDate = SimpleDateFormat("yyy-MM-dd" , Locale.ENGLISH)
        val dateString: String = formatDate.format(date)

        getTrainings(dateString)
      //  checkReservations(dateString)
        setHorizontalCalendar(rootView)



        return rootView
    }

    private fun setHorizontalCalendar(rootView: View) {
        val formatDate = SimpleDateFormat("yyy-MM-dd" , Locale.ENGLISH)
        val startDate: Calendar = Calendar.getInstance()
        startDate.add(Calendar.MONTH, -1)

        val endDate: Calendar = Calendar.getInstance()
        endDate.add(Calendar.MONTH, 1)

        val horizontalCalendar =  HorizontalCalendar.Builder(rootView, R.id.calendarView)
            .range(startDate, endDate)
            .datesNumberOnScreen(5)
            .build()

        horizontalCalendar.calendarListener = object : HorizontalCalendarListener() {
            override fun onDateSelected(date: Calendar?, position: Int) {
                //do something
                date?.let {
                    val dateSelected = formatDate.format((it.time))

                    getTrainings(dateSelected)
                    adapter.notifyDataSetChanged()
                   // checkReservations(dateSelected)
                }
            }
        }
    }


    private fun getTrainings( date: String) {
        val call = apiService.getTrainings(date)
        call.enqueue(object: Callback<ArrayList<Training>> {
            override fun onResponse(call: Call<ArrayList<Training>>,response: Response<ArrayList<Training>>) {
                if(response.isSuccessful){
                    val training = response.body()
                    training?.let {
                        if(training.size > 0){
                            textViewEmpty.text = ""
                        }else{
                            textViewEmpty.text = "No hay entrenos disponibles"
                        }
                            setRecyclerView(training)
                    }
                }
            }
            override fun onFailure(call: Call<ArrayList<Training>>, t: Throwable) {
                activity?.toast(t.localizedMessage)
            }

        })
    }

    private fun setRecyclerView(training: ArrayList<Training>) {
        recycler.setHasFixedSize(true)
        recycler.layoutManager = layoutManager

            adapter = (TrainingAdapter(training,object :RecyclerTrainingListener{
                override fun onClick(training: Training, position: Int) {

                    activity?.toast("Ver!! ${training.id}")
                }

                override fun onReservation(training: Training, position: Int) {
                    registerTraining(training.id)
                    //adapter.notifyDataSetChanged()
                }

                override fun onUnsubscribe(training: Training, position: Int) {
                    activity?.toast("Me desapunto!!")
                }

            }))

            recycler.adapter = adapter


    }

    private fun registerTraining(trainingId: Int) {
        val call = apiService.reservation("Bearer $jwt",trainingId)
        call.enqueue(object: Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if(response.isSuccessful){
                    activity?.toast("Clase reservada")
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                activity?.toast(t.localizedMessage)
            }

        })
    }

    private fun checkReservations(date: String) {
        val call = apiService.checkReservation("Bearer $jwt" , date)
        call.enqueue(object: Callback<CheckResponse> {
            override fun onResponse(call: Call<CheckResponse>, response: Response<CheckResponse>) {
                if(response.isSuccessful){
                    val checkResponse = response.body()
                    checkResponse?.let {
                        if(checkResponse.success){
                            activity?.toast("Apuntado ${checkResponse.training.id}")
                        }else{
                            activity?.toast("No hay reservas")
                        }
                    }
                }
            }

            override fun onFailure(call: Call<CheckResponse>, t: Throwable) {
                activity?.toast(t.localizedMessage)
            }

        } )
    }




}