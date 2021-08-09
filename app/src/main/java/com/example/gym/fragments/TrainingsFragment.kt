package com.example.gym.fragments


import android.content.Intent
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
import com.example.gym.TrainingActivity
import com.example.gym.adapters.TrainingAdapter
import com.example.gym.listeners.RecyclerTrainingListener
import com.example.gym.models.Training
import com.example.gym.toast
import com.google.android.material.navigation.NavigationView
import devs.mulham.horizontalcalendar.HorizontalCalendar
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener
import kotlinx.android.synthetic.main.activity_navigation.*
import kotlinx.android.synthetic.main.fragment_trainings.view.*
import kotlinx.android.synthetic.main.recycler_training.*
import java.text.SimpleDateFormat
import java.util.*


class TrainingsFragment : Fragment() {

    private  val list: ArrayList<Training> by lazy { getTrainings() }
    private  val list2: ArrayList<Training> by lazy { getTrainings2() }

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
                setRecyclerView2()
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
                //activity?.toast("Reserva!!")
                buttonReservation.text = "RESERVADO"
                buttonReservation.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10F)
                buttonReservation.setBackgroundColor(Color.parseColor("#a1e197"))
                textViewEnroll.text = "2/20"
            }

        }))

        recycler.adapter = adapter
    }

    private fun getTrainings2(): ArrayList<Training>{
        return object: ArrayList<Training>(){
            init{
                add(Training(1,1,"06/08/2021" ,"11:00","12:00" , 20 , 2 , "Prueba"))
                add(Training(2,1,"06/08/2021" ,"12:00","13:00" , 5 , 4 , "Prueba"))

            }
        }
    }


    private fun setRecyclerView2() {
        recycler.setHasFixedSize(true)
        recycler.layoutManager = layoutManager
        adapter = (TrainingAdapter(list2,object :RecyclerTrainingListener{
            override fun onClick(training: Training, position: Int) {
                 activity?.toast("Ver!!")
            }

            override fun onReservation(training: Training, position: Int) {
                //activity?.toast("Reserva!!")
                buttonReservation.text = "RESERVADO"
                buttonReservation.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10F)
                buttonReservation.setBackgroundColor(Color.parseColor("#a1e197"))
                textViewEnroll.text = "3/20"
            }

        }))

        recycler.adapter = adapter
    }

    private fun getTrainings(): ArrayList<Training>{
        return object: ArrayList<Training>(){
            init{
                add(Training(1,1,"05/08/2021" ,"11:00","12:00" , 20 , 1 , "Prueba"))
            }
        }
    }


}