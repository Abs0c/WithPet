package com.example.porject

import android.app.ActivityOptions
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Picture
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.porject.MyApplication.Companion.storage
import com.example.porject.databinding.ActivityCommunityDetailBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_community_detail.*
import kotlinx.android.synthetic.main.activity_loading.view.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class CommunityDetail : AppCompatActivity() {
    lateinit var binding : ActivityCommunityDetailBinding
    lateinit var db: FirebaseFirestore
    lateinit var adapter: CommunityDetailAdapter
    var storage = MyApplication.storage
    var storageRef: StorageReference = storage.reference
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
        var imageCheck = intent.getStringExtra("image")
        var id = MyApplication.email.toString()
        id = id.substring(0, id.indexOf("@"))
        if(userID.equals(id) || userID.equals(MyApplication.email.toString())){
            binding.communitydelete.visibility = View.VISIBLE
        }
        val toolbar5 = findViewById<Toolbar>(R.id.toolbar5)
        setSupportActionBar(toolbar5)
        setTitle("")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding.detailtitle.text = title
        binding.contents.text = contents
        binding.date.text = time
        binding.name.text = userID
        db = MyApplication.db
        if(imageCheck.equals("1")){
            val storageReference = storage.reference
            storageReference.child("community/"+noteNo+".jpg").downloadUrl.addOnSuccessListener {
                Uriresult->
                Glide.with(this).load(Uriresult).into(binding.communityimage)
                binding.communityimage.visibility = View.VISIBLE
            }
        }
        binding.communityimage.setOnClickListener {
            //Picture = BitmapFactory.decodeByteArray(note.noteImage, 0, note.noteImage.size)
            val bd : BitmapDrawable = binding.communityimage.drawable as BitmapDrawable
            Picture = bd.bitmap
            val options = ActivityOptions.makeSceneTransitionAnimation(this, binding.communityimage, "imgTrans")
            val intent = Intent(this, ViewActivity::class.java)
            startActivity(intent, options.toBundle())
        }
        adapter = CommunityDetailAdapter(this)
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("MM월 dd일 HH시 mm분")
        val formatted = current.format(formatter)
        binding.upload.setOnClickListener {
            var Title = noteNo.toString()
            var Contents = binding.comment.text.toString()
            var Time = formatted
            var Userid = MyApplication.email.toString()
            Userid = Userid.substring(0, Userid.indexOf("@"))
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
                    val item = CommunityData(document["title"] as String, document["contents"] as String, document["time"] as String, document["userUID"] as String, document["good"].toString(), document["noteNo"].toString())
                    datas.add(item)
                }
                adapter.datas = datas
                adapter.notifyDataSetChanged()
                val layoutManager = LinearLayoutManager(this)
                binding.commentlist.layoutManager = layoutManager
                binding.commentlist.adapter = adapter
            }
        }

        binding.communitydelete.setOnClickListener {
            if (noteNo != null) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("").setMessage("정말 삭제하시겠습니까?")
                    .setPositiveButton("네",
                    DialogInterface.OnClickListener { dialog, which ->
                        db.collection("Question").document("$noteNo").delete()
                        db.collection("Community").document("$noteNo").delete()
                        storage.reference.child("community/"+noteNo+".jpg").delete()
                        finish()
                    }).setNegativeButton("아니오", DialogInterface.OnClickListener { dialog, which ->  })
                builder.show()
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
                    val item = CommunityData(document["title"] as String, document["contents"] as String, document["time"] as String, document["userUID"] as String, document["good"].toString(), document["noteNo"].toString())
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
        datas.clear()
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
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){

            finish()
        }
        return super.onOptionsItemSelected(item)
    }

}