package com.example.porject

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.TextView
import android.widget.Toast
import com.example.porject.databinding.FragmentDiaryBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

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
    lateinit var calendarView: CalendarView
    lateinit var readBtn: Button
    lateinit var diaryTextView: TextView
    lateinit var diaryContent: TextView
    lateinit var title: TextView
    lateinit var binding: FragmentDiaryBinding;

    private lateinit var auth: FirebaseAuth





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View? {


        // Inflate the layout for this fragment
        binding = FragmentDiaryBinding.inflate(layoutInflater)
        calendarView = binding.calendarView
        diaryTextView = binding.diaryTextView
        readBtn = binding.readBtn
        diaryContent = binding.diaryContent
        title = binding.title






        //펫리스트 일단 예시
        val items = arrayOf<String>("뽀삐", "퉁퉁이")


        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            diaryTextView.visibility = View.VISIBLE
            readBtn.visibility = View.VISIBLE

            diaryContent.visibility = View.INVISIBLE
            diaryTextView.text = String.format("%d / %d / %d", year, month + 1, dayOfMonth)

        }

        val auth1 = Firebase.auth.currentUser
        binding.readBtn.setOnClickListener() {

            if (diaryTextView.text != ""){
                val pet_list = Intent(activity,TestActivity::class.java)
                pet_list.putExtra("date", diaryTextView.text)
                startActivity(pet_list)
            }
            else{
                Toast.makeText(activity, "날짜를 선택해주세요", Toast.LENGTH_SHORT).show()
            }
            //다이어리부분으로 넘어가는 창구현


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