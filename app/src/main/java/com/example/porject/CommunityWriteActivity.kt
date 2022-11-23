package com.example.porject

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.porject.databinding.ActivityCommunityWriteBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CommunityWriteActivity : AppCompatActivity() {
    lateinit var binding : ActivityCommunityWriteBinding
    val db = FirebaseFirestore.getInstance()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommunityWriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분")
        val formatted = current.format(formatter)
        binding.communitywriteSave.setOnClickListener {
            if(binding.communitywriteTitle.equals("")) Toast.makeText(this, "제목을 입력해주세요.", Toast.LENGTH_SHORT).show()
            else{
                var Title = binding.communitywriteTitle.text.toString()
                var Contents = binding.communitywriteContents.text.toString()
                var Time = formatted
                var Userid = MyApplication.email.toString()
                var Good = "0"
                var noteNo = (10000000000000 - System.currentTimeMillis()).toString()
                writeCommunity(Title, Contents, Time, Userid, Good, noteNo)
            }
        }
        binding.communitywriteCancel.setOnClickListener {
            finish()
        }
    }
    fun writeCommunity(title : String, contents : String, time : String, userid : String, good : String, noteNo : String){
        val data = CommunityData(title, contents, time, userid, good, noteNo)
        val colRef = db.collection("Community")
        db.collection("Community").document(noteNo).set(data)
        //db.collection("Community").document(noteNo).collection("Comment").document(noteNo).set(data)
        finish()
        /*colRef.add(data).addOnSuccessListener {
            Toast.makeText(this,"저장완료", Toast.LENGTH_SHORT).show()
            finish()
        }
            .addOnFailureListener{
                Toast.makeText(this,"에러", Toast.LENGTH_SHORT).show()
            }*/

    }
}