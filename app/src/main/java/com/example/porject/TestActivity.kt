package com.example.porject

import android.app.ActivityOptions
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.get
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Query
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.grpc.internal.DnsNameResolver
import kotlinx.android.synthetic.main.activity_community_detail.*
import kotlinx.android.synthetic.main.activity_community_detail.view.*

lateinit var Picture : Bitmap
class TestActivity : AppCompatActivity(), NoteClickInterface, NoteCLickDeleteInterface, ImageClickInterface
     {
    lateinit var notesRV: RecyclerView
    lateinit var addFAB: FloatingActionButton
    lateinit var viewModel: NoteViewModel
    lateinit var dateText: String
    lateinit var adapter: NoteRVAdapter
    lateinit var testselectpetspinner: Spinner
    lateinit var name: String
    lateinit var desc: String
    lateinit var img: ByteArray
    lateinit var nameList: ArrayList<Note>

    //private var itemList: MutableList<Note> = mutableListOf()
    //private var titleList: ArrayList<String> = ArrayList()
    //private var descriptList: ArrayList<String> = ArrayList()
    //private var mAdapter: SearchAdapter? = null
    //private var searchView: SearchView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = intent
        dateText = intent.getStringExtra("selectedDate").toString()
        //Toast.makeText(this, dateText, Toast.LENGTH_SHORT).show()
        //name = intent.getStringExtra("name").toString()
        //desc = intent.getStringExtra("desc").toString()
        //img = intent.getByteArrayExtra("img")!!
        //var bitmap = img?.let { BitmapFactory.decodeByteArray(img, 0, it.size) }
        //nameList.add(name, desc, dateText, img)

        setContentView(R.layout.activity_test)
        setTitle("기록 확인")

        val toolbar2 = findViewById<Toolbar>(R.id.toolbar2)
        setSupportActionBar(toolbar2)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)


        notesRV = findViewById(R.id.idRVNotes)
        addFAB = findViewById(R.id.idFABAddNote)

        // itemList = ArrayList()


        //원조코드
        notesRV.layoutManager = LinearLayoutManager(this)

        //notesRV.itemAnimator = DefaultItemAnimator()
        //mAdapter = SearchAdapter(this, titleList as ArrayList<Note>, this)
        //notesRV.adapter = mAdapter


        val noteRVAdapter = NoteRVAdapter(this, this, this, this)
        notesRV.adapter = noteRVAdapter

        testselectpetspinner = findViewById(R.id.test_select_pet_spinner)
        val items = java.util.ArrayList<String>()
        //선택하세요!
        items.add("선택하세요")
        MyApplication.db.collection("pets").get().addOnSuccessListener { result ->
            for (document in result) {
                if (MyApplication.auth.currentUser?.uid == document["userUID"]) {
                    items.add(document["petName"].toString())
                }
            }
            val adapter =
                ArrayAdapter(applicationContext, android.R.layout.simple_spinner_item, items)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            testselectpetspinner.adapter = adapter
        }

        var filterList: List<Note>
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(NoteViewModel::class.java)

        viewModel.allNotes.observe(this, Observer { list ->
            list?.let {
                filterList = it
                testselectpetspinner.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            p0: AdapterView<*>?,
                            p1: View?,
                            p2: Int,
                            p3: Long
                        ) {
                            if (p2 != 0) {
                                filterList =
                                    it.filter { it.petName == testselectpetspinner.selectedItem.toString() }
                                if (dateText != "") {
                                    filterList = filterList.filter { it.timestamp == dateText }
                                }
                            } else {
                                filterList = it
                                if (dateText != "") {
                                    filterList = it.filter { it.timestamp == dateText }
                                }
                            }
                            noteRVAdapter.updateList(filterList)
                        }

                        override fun onNothingSelected(p0: AdapterView<*>?) {
                            TODO("Not yet implemented")
                        }
                    }
                if (dateText != "") {
                    filterList = it.filter { it.timestamp == dateText }
                }
                noteRVAdapter.updateList(it)
                //titleList = ArrayList()
                //descriptList = ArrayList()
                //titleList.add(temp.noteTitle)
                //descriptList.add(temp.noteDescription)
            }
        })
        //Toast.makeText(this, viewModel.allNotes.value.toString(), Toast.LENGTH_SHORT).show()
        addFAB.setOnClickListener {
            val intent = Intent(this@TestActivity, AddEitNoteActivity::class.java)
            startActivity(intent)
            this.finish()
            overridePendingTransition(R.anim.slide_up_enter, R.anim.slide_up_exit)
        }



        adapter = NoteRVAdapter(this, this, this, this)
        adapter.switchLayout(1)


    }


    /*private fun fillItem(){//: MutableList<Note>{
        //Toast.makeText(this, viewModel.allNotes.value.toString(), Toast.LENGTH_SHORT).show()
       //return itemList << itemList에 노트 요소("제목") 옮기고 리턴해줌
    }*/





    override fun onDeleteIconClick(note: Note) {
        viewModel.deleteNote(note)
        Toast.makeText(this, "${note.noteTitle} 삭제됨", Toast.LENGTH_LONG).show()
    }

    override fun onNoteClick(note: Note) {
        val intent = Intent(this@TestActivity, AddEitNoteActivity::class.java)
        intent.putExtra("noteType", "Edit")
        intent.putExtra("noteTitle", note.noteTitle)
        intent.putExtra("petname", note.petName)
        intent.putExtra("noteDescription", note.noteDescription)
        intent.putExtra("image", note.noteImage)
        intent.putExtra("noteID", note.id)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_up_enter, R.anim.slide_up_exit)
        this.finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun ImageClick(note: Note, view: View) {
        Picture = BitmapFactory.decodeByteArray(note.noteImage, 0, note.noteImage.size)
        val options = ActivityOptions.makeSceneTransitionAnimation(this, view, "imgTrans")
        val intent = Intent(this, ViewActivity::class.java)
        startActivity(intent, options.toBundle())
    }

    override fun onBackPressed() {
        super.onBackPressed()
        //supportFinishAfterTransition()
        overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit)
    }

    fun clickEvent(view: View) {
        val intent = Intent(this, ViewActivity::class.java)
        val opt = ActivityOptions.makeSceneTransitionAnimation(this, view, "img_trans")
        startActivity(intent, opt.toBundle())
    }





    }


