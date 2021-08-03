package com.example.gym


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gym.adapters.TrainingAdapter
import com.example.gym.listeners.RecyclerTrainingListener
import com.example.gym.models.Training
import com.google.android.material.navigation.NavigationView
import devs.mulham.horizontalcalendar.HorizontalCalendar
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener
import kotlinx.android.synthetic.main.activity_navigation.*
import kotlinx.android.synthetic.main.fragment_trainings.*
import kotlinx.android.synthetic.main.fragment_trainings.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class TrainingsFragment : Fragment() {

    private  val list: ArrayList<Training> by lazy { getTrainings() }

    private lateinit var recycler: RecyclerView
    private lateinit var adapter: TrainingAdapter
    private val layoutManager by lazy { LinearLayoutManager(context) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        activity?.setTitle("Calendario")

        val navigationView = activity?.nav_view as NavigationView
        navigationView.setCheckedItem(R.id.nav_calendar)

        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_trainings, container, false)

        recycler = rootView.recyclerView as RecyclerView
        setRecyclerView()


        // Calendar horizontal
        val date = System.currentTimeMillis()
        val formatDate = SimpleDateFormat("dd/MM/yyyy")
        val dateString: String = formatDate.format(date)

       // rootView.textView.text = dateString

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
                val formatDate = SimpleDateFormat("dd/MM/yyyy")
                val dateCurrent = formatDate.format((date?.time))
                //textView.text = dateCurrent
            }
        }
        //view.label_date.text = dateString


        return rootView
    }

    private fun setRecyclerView() {
        recycler.setHasFixedSize(true)
        recycler.layoutManager = layoutManager
        adapter = (TrainingAdapter(list,object :RecyclerTrainingListener{
            override fun onClick(training: Training, position: Int) {
                activity?.toast("Ver!!")
            }

            override fun onReservation(training: Training, position: Int) {
                activity?.toast("Reserva!!")
            }

        }))

        recycler.adapter = adapter
    }

    private fun getTrainings(): ArrayList<Training>{
        return object: ArrayList<Training>(){
            init{
                add(Training(1,1,"20/07/2021" ,"11:00","12:00" , 20 , 2 , "Prueba"))
                add(Training(2,1,"20/07/2021" ,"12:00","13:00" , 20 , 10 , "Prueba"))
                add(Training(3,1,"20/07/2021" ,"13:00","14:00" , 20 , 20 , "Prueba"))
                add(Training(4,1,"20/07/2021" ,"14:00","15:00" , 20 , 4 , "Prueba"))
            }
        }
    }


}