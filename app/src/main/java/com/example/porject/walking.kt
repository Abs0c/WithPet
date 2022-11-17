package com.example.porject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.porject.databinding.ActivityWalkingBinding
import com.example.porject.databinding.ActivityWalkingWriteBinding
import kotlinx.android.synthetic.main.activity_walking.*
import kotlinx.android.synthetic.main.activity_walking.view.*

/*class walking : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: walkingAdapter
    lateinit var binding: ActivityWalkingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWalkingBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_walking)
        var rootView = findViewById(R.id.walking_list) as ViewGroup
        initUI(rootView)
    }
    private fun initUI(rootView : ViewGroup){
        adapter = walkingAdapter(arrayListOf<walkingData>())
        recyclerView = rootView.walking_list
        rootView.rootView.switch1.setOnClickListener{
            if(rootView.rootView.switch1.isChecked){
                adapter.switchLayout(1)
                adapter.notifyDataSetChanged()
            }
            else{
                adapter.switchLayout(0)
                adapter.notifyDataSetChanged()
            }
        }
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        adapter.addItem(
            walkingData(
                0,
                "0",
                "전주시 덕진구",
                "123",
                "",
                "1. 테스트중!",
                "0",
                "capture1.jpg",
                "3월 14일"
            )
        )
        recyclerView.adapter = adapter
        adapter.setOnItemClickListener(object : OnNoteItemClickListener{
            override fun onItemClick(holder: walkingAdapter.ViewHolder, view: View, position: Int) {
                val item = adapter.getItem(position)
                Toast.makeText(this@walking, "선택됨" + item?.contents, Toast.LENGTH_SHORT).show()
            }
        })
    }

}*/