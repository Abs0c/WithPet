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
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

lateinit var Picture : Bitmap
class TestActivity : AppCompatActivity(), NoteClickInterface, NoteCLickDeleteInterface, ImageClickInterface
    ,SearchAdapter.ItemClickListener{
    lateinit var notesRV:RecyclerView
    lateinit var addFAB: FloatingActionButton
    lateinit var viewModel: NoteViewModel
    lateinit var dateText: String
    lateinit var adapter: NoteRVAdapter

    private var itemList: MutableList<Note> = mutableListOf()
    private var titleList: ArrayList<String> = ArrayList()
    private var descriptList: ArrayList<String> = ArrayList()
    private var mAdapter: SearchAdapter? = null
    private var searchView: SearchView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = intent
        dateText = intent.getStringExtra("selectedDate").toString()
        //Toast.makeText(this, dateText, Toast.LENGTH_SHORT).show()
        setContentView(R.layout.activity_test)
        setTitle("기록 확인")
        val toolbar2 = findViewById<Toolbar>(R.id.toolbar2)
        setSupportActionBar(toolbar2)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)


        notesRV = findViewById(R.id.idRVNotes)
        addFAB = findViewById(R.id.idFABAddNote)

        itemList = ArrayList()




        //원조코드
        notesRV.layoutManager = LinearLayoutManager(this)

        notesRV.itemAnimator = DefaultItemAnimator()
        //mAdapter = SearchAdapter(this, titleList as ArrayList<Note>, this)
        //notesRV.adapter = mAdapter



        val noteRVAdapter = NoteRVAdapter(this, this, this, this)
        notesRV.adapter = noteRVAdapter


        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(NoteViewModel::class.java)
        viewModel.allNotes.observe(this, Observer { list -> list?.let{
                if (dateText != ""){
                    var notelistbydate: List<Note> = it.filter { it.timestamp == dateText }
                    noteRVAdapter.updateList(notelistbydate)
                }
                else {
                    noteRVAdapter.updateList(it)
                }
                //titleList = ArrayList()
                //descriptList = ArrayList()
                    //titleList.add(temp.noteTitle)
                    //descriptList.add(temp.noteDescription)
            }
        })
        //Toast.makeText(this, viewModel.allNotes.value.toString(), Toast.LENGTH_SHORT).show()
        addFAB.setOnClickListener{
            val intent = Intent(this@TestActivity, AddEitNoteActivity::class.java)
            startActivity(intent)
            this.finish()
        }



        adapter = NoteRVAdapter(this, this, this, this)
        adapter.switchLayout(1)



    }

    /*private fun fillItem(){//: MutableList<Note>{
        //Toast.makeText(this, viewModel.allNotes.value.toString(), Toast.LENGTH_SHORT).show()
       //return itemList << itemList에 노트 요소("제목") 옮기고 리턴해줌
    }*/


    override fun onItemClicked(item: Note) {
        val intent = Intent(this@TestActivity, AddEitNoteActivity::class.java)
        intent.putExtra("noteType", "Edit")
        intent.putExtra("noteTitle", item.noteTitle)
        intent.putExtra("noteDescription", item.noteDescription)
        intent.putExtra("image",item.noteImage)
        intent.putExtra("noteID", item.id)
        startActivity(intent)
        this.finish()

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
    /*
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        val item = menu?.findItem(R.id.search_bar)
        var searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = item?.actionView as SearchView
        searchView!!.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView!!.maxWidth = Integer.MAX_VALUE
        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                mAdapter!!.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                mAdapter!!.filter.filter(query)
                return false
            }
        })
        return true
    }
    */
    /*

     */



}