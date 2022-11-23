package com.example.porject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.porject.databinding.ActivityCommunityBinding
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_community.*

class CommunityActivity : AppCompatActivity() {
    lateinit var binding: ActivityCommunityBinding
    lateinit var adapter: CommunityAdapter
    lateinit var db: FirebaseFirestore
    val datas = mutableListOf<CommunityData>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommunityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var recyclerView = findViewById<RecyclerView>(R.id.communitylist)
        binding.write.setOnClickListener {
            intent = Intent(this, CommunityWriteActivity::class.java)
            startActivity(intent)
        }
        if(MyApplication.checkAuth()) {}
        db = MyApplication.db
        adapter = CommunityAdapter(this, arrayListOf<CommunityData>())
        db.collection("Community")
            .get()
            .addOnSuccessListener { result ->
                for (document in result){
                    //val item = CommunityData(document["title"] as String, document["contents"] as String, document["time"] as String, document["userUID"] as String, document["good"] as Int)
                    val item = CommunityData(document["title"] as String, document["contents"] as String, document["time"] as String, document["userUID"] as String, document["good"].toString(), document["noteNo"].toString())
                    //val item = CommunityData()
                    datas.add(item)
                    adapter.datas = datas
                    adapter.notifyDataSetChanged()
                    val layoutManager = LinearLayoutManager(this)
                    binding.communitylist.layoutManager = layoutManager
                    binding.communitylist.adapter = adapter
                }

        }
        /*adapter.datas = datas
        adapter.notifyDataSetChanged()
        val layoutManager = LinearLayoutManager(this)
        binding.communitylist.layoutManager = layoutManager
        binding.communitylist.adapter = adapter*/
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
                    val item = CommunityData(document["title"] as String, document["contents"] as String, document["time"] as String, document["userUID"] as String, document["good"].toString(), document["noteNo"].toString())
                    //val item = CommunityData()
                    datas.add(item)
                    adapter.datas = datas
                    adapter.notifyDataSetChanged()
                    val layoutManager = LinearLayoutManager(this)
                    binding.communitylist.layoutManager = layoutManager
                    binding.communitylist.adapter = adapter
                }

            }
    }
}