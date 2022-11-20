package com.example.porject

import android.app.ActivityOptions
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Picture
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
lateinit var Picture : Bitmap
class TestActivity : AppCompatActivity(), NoteClickInterface, NoteCLickDeleteInterface, ImageClickInterface{
    lateinit var notesRV:RecyclerView
    lateinit var addFAB: FloatingActionButton
    lateinit var viewModel: NoteViewModel
    lateinit var dateText: String
    lateinit var adapter: NoteRVAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = intent
        dateText = intent.getStringExtra("date").toString()
        Toast.makeText(this, dateText, Toast.LENGTH_SHORT).show()
        setContentView(R.layout.activity_test)
        setTitle("기록 확인")
        val toolbar2 = findViewById<Toolbar>(R.id.toolbar2)
        setSupportActionBar(toolbar2)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        notesRV = findViewById(R.id.idRVNotes)
        addFAB = findViewById(R.id.idFABAddNote)
        notesRV.layoutManager = LinearLayoutManager(this)

        val noteRVAdapter = NoteRVAdapter(this, this, this, this)
        notesRV.adapter = noteRVAdapter
        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(NoteViewModel::class.java)
        viewModel.allNotes.observe(this, Observer { list -> list?.let{
                noteRVAdapter.updateList(it)
            }
        })
        addFAB.setOnClickListener{
            val intent = Intent(this@TestActivity, AddEitNoteActivity::class.java)
            startActivity(intent)
            this.finish()
        }
        adapter = NoteRVAdapter(this, this, this, this)
        adapter.switchLayout(1)
    }
    override fun onDeleteIconClick(note: Note) {
        viewModel.deleteNote(note)
        Toast.makeText(this, "${note.noteTitle} 삭제됨", Toast.LENGTH_LONG).show()
    }
    override fun onNoteClick(note: Note) {
        val intent = Intent(this@TestActivity, AddEitNoteActivity::class.java)
        intent.putExtra("noteType", "Edit")
        intent.putExtra("noteTitle", note.noteTitle)
        intent.putExtra("noteDescription", note.noteDescription)
        intent.putExtra("image",note.noteImage)
        intent.putExtra("noteID", note.id)
        startActivity(intent)
        this.finish()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun ImageClick(note: Note) {
        Picture = BitmapFactory.decodeByteArray(note.noteImage, 0, note.noteImage.size)
        val intent = Intent(this, ViewActivity::class.java)
        startActivity(intent)

    }
    fun clickEvent(view : View){
        val intent = Intent(this, ViewActivity::class.java)
        val opt = ActivityOptions.makeSceneTransitionAnimation(this, view, "img_trans")
        startActivity(intent, opt.toBundle())
    }

}