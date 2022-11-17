package com.example.porject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.github.channguyen.rsv.RangeSliderView

class walkingWrite : AppCompatActivity() {
    lateinit var contents : EditText
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_walking_write)
        contents = findViewById(R.id.idEdtNoteDescription)

        var saveButton = findViewById<Button>(R.id.idBtnAddupdate)
        var deleteButton = findViewById<Button>(R.id.deleteButton)
        var cancleButton = findViewById<Button>(R.id.closeButton)
        var sliderView = findViewById<RangeSliderView>(R.id.sliderView)
        saveButton.setOnClickListener {
            val content = contents.text.toString()

        }
        sliderView.setOnSlideListener {
                index -> Toast.makeText(this, "moodIndex changed to $index", Toast.LENGTH_SHORT).show()
        }
        cancleButton.setOnClickListener {
            finish()
        }

    }
}