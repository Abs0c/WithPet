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

class walking : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: walkingAdapter
    lateinit var binding: ActivityWalkingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWalkingBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_walking)

    }


}