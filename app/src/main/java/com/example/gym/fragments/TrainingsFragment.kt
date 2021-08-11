package com.example.gym.fragments



import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gym.R
import com.example.gym.adapters.TrainingAdapter
import com.example.gym.io.ApiService
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


class TrainingsFragment : Fragment() {

   // private  val list: ArrayList<Training> by lazy { getTrainings() }

    private lateinit var recycler: RecyclerView
    private lateinit var adapter: TrainingAdapter
    private val layoutManager by lazy { LinearLayoutManager(context) }

    private val apiService: ApiService by lazy {
        ApiService.create()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        activity?.setTitle("Calendario")

        val navigationView = activity?.nav_view as NavigationView
        navigationView.setCheckedItem(R.id.nav_calendar)

        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_trainings, container, false)

        recycler = rootView.recyclerView as RecyclerView
       // setRecyclerView()

        // Calendar horizontal
        val date = System.currentTimeMillis()
        val formatDate = SimpleDateFormat("yyy-MM-dd" , Locale.ENGLISH)
        val dateString: String = formatDate.format(date)

        getTrainings(dateString)

        //rootView.textViewPrueba.text = dateString

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
                    val dateCurrent = formatDate.format((it.time))
                    //textViewPrueba.text = dateCurrent
                    getTrainings(dateCurrent)
                    adapter.notifyDataSetChanged()
                }
               // setRecyclerView2()
            }
        }
        //view.label_date.text = dateString


        return rootView
    }

    private fun getTrainings( date: String) {
        val call = apiService.getTrainings(date)
        call.enqueue(object: Callback<ArrayList<Training>> {
            override fun onResponse(call: Call<ArrayList<Training>>,response: Response<ArrayList<Training>>) {
                if(response.isSuccessful){
                    val training = response.body()
                    training?.let {
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
                activity?.toast("Ver!!")
            }

            override fun onReservation(training: Training, position: Int) {
                //activity?.toast("Reserva!!")
                buttonReservation.text = "RESERVADO"
                buttonReservation.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10F)
                buttonReservation.setBackgroundColor(Color.parseColor("#a1e197"))
                textViewEnroll.text = "2/20"
            }

        }))

        recycler.adapter = adapter
    }




  /*  private fun getTrainings(): ArrayList<Training>{
        return object: ArrayList<Training>(){
            init{
                add(Training(1,1,"05/08/2021" ,"11:00","12:00" , 20 , 1 , "Prueba"))
            }
        }
    }*/


}