package com.example.porject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.porject.MyApplication.Companion.db
import com.example.porject.databinding.ActivityMypetBinding
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_my_pet.*

class activity_mypet : AppCompatActivity() {
    lateinit var binding : ActivityMypetBinding
    lateinit var db: FirebaseFirestore
    var items = mutableListOf<myPetType>()
    var adapter: myListAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMypetBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.addPetButton.setOnClickListener{
                val intent = Intent(this, AddpetActivity::class.java)
                startActivity(intent)

        }
        if(!MyApplication.checkAuth()){
            binding.listView.visibility = View.INVISIBLE
            add_pet_button.visibility = View.INVISIBLE
        }
        else{
            add_pet_button.visibility = View.VISIBLE
            binding.listView.visibility = View.VISIBLE
            db = MyApplication.db
            items.clear()
            db.collection("pets")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result){
                        if (document["userUID"] as String? == MyApplication.auth.currentUser?.uid){
                            val item = myPetType(document["petName"] as String, document["petType"] as Long, document["petWeight"].toString().toDouble(), document["userUID"] as String?)
                            items.add(item)
                        }
                    }
                    adapter = myListAdapter(this, items)
                    binding.listView.adapter = adapter
                    binding.listView.layoutManager = LinearLayoutManager(this)
                }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.fadein, R.anim.fadeout)
    }
}