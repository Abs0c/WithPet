package com.example.porject

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.porject.databinding.ActivityDiaryPetBinding
import com.google.firebase.firestore.FirebaseFirestore

class DiaryPet: AppCompatActivity() {
    lateinit var binding: ActivityDiaryPetBinding
    lateinit var db: FirebaseFirestore
    var adapter: myListAdapter? = null
    var items = mutableListOf<myPetType>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle("펫 리스트")
        binding = ActivityDiaryPetBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val toolbar = findViewById<Toolbar>(R.id.pet_list_tool)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
// 펫리스트 구현부분
// 레이아웃에 activity_diary_pet에 list_pet이라는 리스트뷰 아이디 설정
        //list_pet에 펫 이름만이라도 데이터베이스에서 가져오려고함
        /*if(!MyApplication.checkAuth()){
            Toast.makeText(this, "로그인이 필요합니다.", Toast.LENGTH_LONG).show()
        }
        else {
            db = MyApplication.db
            adapter = myListAdapter(this, items)
            val useruid = MyApplication.auth.currentUser?.uid

            db.collection("pets")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        if (document["userUID"] as String? == useruid) {
                            val item = myPetType(document["petName"] as String, document["petType"] as String, document["userUID"] as String?)
                            items.add(item)
                        }
                    }
                    binding.listPet.adapter = adapter
                    binding.listPet.layoutManager = LinearLayoutManager(this)
                }
        }*/
    }

    override fun onStart() {
        super.onStart()
        if(!MyApplication.checkAuth()){
            Toast.makeText(this, "로그인이 필요합니다.", Toast.LENGTH_LONG).show()
        }
        else {
            db = MyApplication.db
            adapter = myListAdapter(this, items)
            val useruid = MyApplication.auth.currentUser?.uid
            db.collection("pets")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        if (document["userUID"] as String? == useruid) {
                            val item = myPetType(document["petName"] as String, document["petType"] as Long, document["petWeight"] as Double, document["userUID"] as String?)
                            items.add(item)
                        }
                    }
                    binding.listPet.adapter = adapter
                    binding.listPet.layoutManager = LinearLayoutManager(this)
                }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            finish()
        }
        return super.onOptionsItemSelected(item)
    }


}