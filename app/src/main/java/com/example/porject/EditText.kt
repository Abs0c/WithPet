package com.example.porject

import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.porject.MyApplication.Companion.db

class EditText : AppCompatActivity(){

    //private var db = DBHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle("일기장 작성")
        setContentView(R.layout.activity_edit)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val editText = findViewById<EditText>(R.id.editT)
        val savedButton = findViewById<Button>(R.id.btnSave)
        savedButton.setOnClickListener {
            //db.addMemo(db.writableDatabase, editText.text.toString())
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}