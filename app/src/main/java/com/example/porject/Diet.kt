package com.example.porject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.porject.databinding.ActivityDietBinding

class Diet : AppCompatActivity() {
    lateinit var binding: com.example.porject.databinding.ActivityDietBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDietBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }

}