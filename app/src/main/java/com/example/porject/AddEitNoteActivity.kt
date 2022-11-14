package com.example.porject

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider

import java.text.SimpleDateFormat
import java.util.*

class AddEitNoteActivity : AppCompatActivity() {
    lateinit var noteTitleEdt : EditText
    lateinit var noteDescriptionEdt : EditText
    lateinit var addUpdateBtn : Button
    lateinit var viewModel: NoteViewModel
    private val GALLERY = 1
    var noteID = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_eit_note)
        setTitle("기록 저장")
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        noteTitleEdt = findViewById<EditText>(R.id.idEdtNoteTitle)
        noteDescriptionEdt = findViewById<EditText>(R.id.idEdtNoteDescription)
        addUpdateBtn = findViewById(R.id.idBtnAddUpdate)
        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))
            .get(NoteViewModel::class.java)
        val imgbtn = findViewById<ImageButton>(R.id.imBtn2)
        imgbtn.setOnClickListener {
            val intent: Intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.setType("image/*")
            startActivityForResult(intent, GALLERY)
        }
        val noteType = intent.getStringExtra("noteType")
        if(noteType.equals("Edit")){
            val noteTitle = intent.getStringExtra("noteTitle")
            val noteDesc = intent.getStringExtra("noteDescription")
            noteID = intent.getIntExtra("noteID", -1)
            addUpdateBtn.setText("기록 변경")
            noteTitleEdt.setText(noteTitle)
            noteDescriptionEdt.setText(noteDesc)
        }else{
            addUpdateBtn.setText("기록 저장")
        }

        addUpdateBtn.setOnClickListener {
            val noteTitle = noteTitleEdt.text.toString()
            val noteDescription = noteDescriptionEdt.text.toString()

            if(noteType.equals("Edit")){
                if(noteTitle.isNotEmpty() && noteDescription.isNotEmpty()){
                    val sdf = SimpleDateFormat("dd MMM, yyyy - HH:mm")
                    val currentDate:String = sdf.format(Date())
                    val updateNote = Note(noteTitle, noteDescription, currentDate)
                    updateNote.id = noteID
                    viewModel.updateNote(updateNote)
                    Toast.makeText(this, "기록 업데이트중..", Toast.LENGTH_LONG).show()
                }
            }else{
                if(noteTitle.isNotEmpty() && noteDescription.isNotEmpty())
                {
                    val sdf = SimpleDateFormat("dd MMM, yyyy - HH:mm")
                    val currentDate:String = sdf.format(Date())
                    viewModel.addNote(Note(noteTitle,noteDescription, currentDate))
                    Toast.makeText(this, "기록 추가중..", Toast.LENGTH_LONG).show()
                }
            }
            startActivity(Intent(applicationContext, TestActivity::class.java))
            this.finish()
        }

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            startActivity(Intent(applicationContext, TestActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }
    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val imBtn: ImageButton = findViewById(R.id.imBtn2)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY) {
                var ImnageData: Uri? = data?.data
                Toast.makeText(this, ImnageData.toString(), Toast.LENGTH_SHORT).show()
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, ImnageData)
                    imBtn.setImageBitmap(bitmap)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}