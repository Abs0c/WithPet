package com.example.porject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.porject.databinding.ActivityCommunityBinding
import com.google.firebase.firestore.FirebaseFirestore

class CommunityActivity : AppCompatActivity() {
    lateinit var binding: ActivityCommunityBinding
    lateinit var adapter: CommunityAdapter
    lateinit var db: FirebaseFirestore
    val datas = mutableListOf<CommunityData>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommunityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val toolbar4 = findViewById<Toolbar>(R.id.toolbar4)
        setSupportActionBar(toolbar4)
        setTitle("")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        var recyclerView = findViewById<RecyclerView>(R.id.questionlist)
        binding.write.setOnClickListener {
            intent = Intent(this, CommunityWriteActivity::class.java)
            intent.putExtra("mode", "Community")
            startActivity(intent)
            overridePendingTransition(R.anim.slide_up_enter, R.anim.slide_up_exit)
        }
        if(MyApplication.checkAuth()) {}
        db = MyApplication.db
        adapter = CommunityAdapter(this, arrayListOf<CommunityData>())
        db.collection("Community")
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
                binding.communitylist.layoutManager = layoutManager
                binding.communitylist.adapter = adapter

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
        db.collection("Community")
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
                binding.communitylist.layoutManager = layoutManager
                binding.communitylist.adapter = adapter

            }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            finish()
            overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit)

        }
        return super.onOptionsItemSelected(item)
    }
}