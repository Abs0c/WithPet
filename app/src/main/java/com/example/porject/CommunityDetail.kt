package com.example.porject

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.porject.databinding.ActivityCommunityDetailBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CommunityDetail : AppCompatActivity() {
    lateinit var binding : ActivityCommunityDetailBinding
    lateinit var db: FirebaseFirestore
    lateinit var adapter: CommunityDetailAdapter
    val datas = mutableListOf<CommunityData>()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommunityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var title = intent.getStringExtra("title")
        var contents = intent.getStringExtra("contents")
        var time = intent.getStringExtra("time")
        var userID = intent.getStringExtra("userID")
        var good = intent.getStringExtra("good")
        var noteNo = intent.getStringExtra("noteNo")
        binding.detailtitle.text = title
        binding.contents.text = contents
        binding.date.text = time
        binding.name.text = userID
        db = MyApplication.db
        adapter = CommunityDetailAdapter(this)
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분")
        val formatted = current.format(formatter)
        binding.upload.setOnClickListener {
            var Title = ""
            var Contents = binding.comment.text.toString()
            var Time = formatted
            var Userid = MyApplication.email.toString()
            var Good = "0"
            var commentsNo = System.currentTimeMillis().toString()
            if (noteNo != null) {
                writeComments(Title, Contents, Time, Userid, Good, noteNo, commentsNo)
            }
        }
        if (noteNo != null) {
            //Toast.makeText(this, "$noteNo", Toast.LENGTH_SHORT).show()
            db.collection("Community").document(noteNo).collection("Comments").get().addOnSuccessListener { result->
                for (document in result){
                    val item = CommunityData("", document["contents"] as String, document["time"] as String, document["userUID"] as String, document["good"].toString(), document["noteNo"].toString())
                    datas.add(item)
                }
                adapter.datas = datas
                adapter.notifyDataSetChanged()
                val layoutManager = LinearLayoutManager(this)
                binding.commentlist.layoutManager = layoutManager
                binding.commentlist.adapter = adapter
            }
        }
    }
    fun writeComments(title : String, contents : String, time : String, userid : String, good : String, noteNo : String, commentsNo : String){
        val data = CommunityData(title, contents, time, userid, good, commentsNo)
        datas.clear()
        binding.comment.setText(null)
        db.collection("Community").document(noteNo).collection("Comments").document(commentsNo).set(data)
        if (noteNo != null) {

            db.collection("Community").document(noteNo).collection("Comments").get().addOnSuccessListener { result->
                for (document in result){
                    val item = CommunityData("", document["contents"] as String, document["time"] as String, document["userUID"] as String, document["good"].toString(), document["noteNo"].toString())
                    datas.add(item)
                }
                adapter.datas = datas
                adapter.notifyDataSetChanged()
                val layoutManager = LinearLayoutManager(this)
                binding.commentlist.layoutManager = layoutManager
                binding.commentlist.adapter = adapter
            }
        }
    }

    /*override fun onRestart() {
        super.onRestart()
        var noteNo = intent.getStringExtra("noteNo")
        if (noteNo != null) {

            db.collection("Community").document(noteNo).collection("Comments").get().addOnSuccessListener { result->
                for (document in result){
                    val item = CommunityData("", document["contents"] as String, document["time"] as String, document["userUID"] as String, document["good"].toString(), document["noteNo"].toString())
                    datas.add(item)
                }
                adapter.datas = datas
                adapter.notifyDataSetChanged()
                val layoutManager = LinearLayoutManager(this)
                binding.commentlist.layoutManager = layoutManager
                binding.commentlist.adapter = adapter
            }
        }
    }*/

}