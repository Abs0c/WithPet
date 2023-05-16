package com.example.porject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.porject.databinding.ActivityQuestionCommunityBinding
import com.google.firebase.firestore.FirebaseFirestore

class QuestionCommunity : AppCompatActivity() {
    lateinit var binding: ActivityQuestionCommunityBinding
    lateinit var adapter: CommunityAdapter
    lateinit var db: FirebaseFirestore
    val datas = mutableListOf<CommunityData>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionCommunityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var recyclerView = findViewById<RecyclerView>(R.id.questionlist)
        binding.write.setOnClickListener {
            intent = Intent(this, CommunityWriteActivity::class.java)
            intent.putExtra("mode", "Question")
            startActivity(intent)
            overridePendingTransition(R.anim.slide_up_enter, R.anim.slide_up_exit)
        }
        val toolbar = findViewById<Toolbar>(R.id.toolbar7)
        setSupportActionBar(toolbar)
        setTitle("")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        if(!MyApplication.checkAuth()) {binding.write.visibility = View.INVISIBLE}
        db = MyApplication.db
        adapter = CommunityAdapter(this, arrayListOf<CommunityData>())
        db.collection("Question")
            .get()
            .addOnSuccessListener { result ->
                for (document in result){
                    //val item = CommunityData(document["title"] as String, document["contents"] as String, document["time"] as String, document["userUID"] as String, document["good"] as Int)
                    val item = CommunityData(document["title"] as String, document["contents"] as String, document["time"] as String, document["userUID"] as String, document["good"].toString(), document["noteNo"].toString(), document["image"].toString())
                    //val item = CommunityData()
                    datas.add(item)
                }
                adapter.datas = datas
                adapter.notifyDataSetChanged()
                val layoutManager = LinearLayoutManager(this)
                binding.questionlist.layoutManager = layoutManager
                binding.questionlist.adapter = adapter

            }
        /*adapter.datas = datas
        adapter.notifyDataSetChanged()
        val layoutManager = LinearLayoutManager(this)
        binding.communitylist.layoutManager = layoutManager
        binding.communitylist.adapter = adapter*/
    }
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit)
    }
    override fun onRestart() {
        super.onRestart()
        datas.clear()
        db = MyApplication.db
        db.collection("Question")
            .get()
            .addOnSuccessListener { result ->
                for (document in result){
                    //val item = CommunityData(document["title"] as String, document["contents"] as String, document["time"] as String, document["userUID"] as String, document["good"] as Int)
                    val item = CommunityData(document["title"] as String, document["contents"] as String, document["time"] as String, document["userUID"] as String, document["good"].toString(), document["noteNo"].toString(), document["image"].toString())
                    //val item = CommunityData()
                    datas.add(item)
                }
                adapter.datas = datas
                adapter.notifyDataSetChanged()
                val layoutManager = LinearLayoutManager(this)
                binding.questionlist.layoutManager = layoutManager
                binding.questionlist.adapter = adapter

            }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}