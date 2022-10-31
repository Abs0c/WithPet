package com.example.porject

import android.R
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.porject.databinding.FragmentDiaryBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
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
        title.text = "달력 일기장"





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
            var db: FirebaseFirestore = FirebaseFirestore.getInstance()

            AlertDialog.Builder(requireContext()).run {
                setTitle("펫목록")
                setIcon(R.drawable.ic_dialog_info)
                setItems(items, object : DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        Log.d("kkang", "선택강아지:${items[p1]}")

                    }
                })
                setPositiveButton("닫기", null)
                show()

            }
        }
        binding.btnDial.setOnClickListener {
            val intent = Intent(getActivity(), EditText::class.java)
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