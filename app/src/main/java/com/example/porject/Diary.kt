package com.example.porject

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.porject.databinding.FragmentDiaryBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Diary.newInstance] factory method to
 * create an instance of this fragment.
 */
class Diary : Fragment() {
//    // TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null

    var userID: String = "userID"
    lateinit var fname: String
    lateinit var str: String
    //    lateinit var calendarView: CalendarView
    lateinit var readBtn: Button
    lateinit var diaryTextView: TextView
    lateinit var diaryContent: TextView
    lateinit var title: TextView
    lateinit var binding: FragmentDiaryBinding
    //lateinit var allNotes: LiveData<List<Note>>

    private lateinit var selectedDate: String // 달력에서 선택한 날짜

    lateinit var calendar: MaterialCalendarView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View? {

        //allNotes = NoteRepository(Room.databaseBuilder(TestActivity().applicationContext, NoteDatabase::class.java, "note_database").build().getNotesDao()).allNotes
        //val dao = NoteDatabase.getDatabase(TestActivity().apply { startActivity(Intent(this, TestActivity::class.java)) }.application).getNotesDao()
        //allNotes = dao?.let { NoteRepository(it).allNotes }!!
        //Toast.makeText(context, allNotes.value.toString(), Toast.LENGTH_SHORT).show()


        // Inflate the layout for this fragment
        binding = FragmentDiaryBinding.inflate(layoutInflater)
        calendar = binding.calendarView
        diaryTextView = binding.diaryTextView
        readBtn = binding.readBtn
        diaryContent = binding.diaryContent
        title = binding.title


        // 날짜 선택시 그 숫자 아래에 점을 추가한다
        calendar.setOnDateChangedListener(object: OnDateSelectedListener {
            override fun onDateSelected(widget: MaterialCalendarView, date: CalendarDay, selected: Boolean) {
                calendar.addDecorator(EventDecorator(Collections.singleton(date)))
            }
        })

       calendar.addDecorators(
           //일요일에 색칠하기
           SundayDecorator(),
           //토요일에 색칠하기
           SaturdayDecorator(),
           //오늘 날짜에 색칠하기
           Decorator(),
        )

        calendar.setOnDateChangedListener(object: OnDateSelectedListener {
            override fun onDateSelected(widget: MaterialCalendarView, date: CalendarDay, selected: Boolean) {
                binding.readBtn.setOnClickListener() {

                    val year = calendar.selectedDate!!.year
                    val month = calendar.selectedDate!!.month+1
                    val day = calendar.selectedDate!!.day
                    selectedDate = "$year-$month-$day"
                    Toast.makeText(activity,"$year"+"년"+"$month"+"월"+"$day"+"일", Toast.LENGTH_SHORT).show()
//                    val pet_list = Intent(activity,TestActivity::class.java)
//                    pet_list.putExtra("date", diaryTextView.text)
//                    startActivity(pet_list)
                }
            }
        })
        binding.readBtn.setOnClickListener(){
            Toast.makeText(activity, "날짜를 선택해 주세요", Toast.LENGTH_SHORT).show()
        }
        binding.btnDial.setOnClickListener {
            val intent = Intent(activity, AddEitNoteActivity::class.java)
            startActivity(intent)
        }

        return binding.root
//        return inflater.inflate(R.layout.fragment_diary, container, false)
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Diary.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Diary().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
            }
    }
}