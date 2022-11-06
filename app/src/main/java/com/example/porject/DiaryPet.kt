package com.example.porject

import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DiaryPet: AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle("펫 리스트")
        setContentView(R.layout.activity_diary_pet)
        val toolbar = findViewById<Toolbar>(R.id.pet_list_tool)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
// 펫리스트 구현부분
// 레이아웃에 activity_diary_pet에 list_pet이라는 리스트뷰 아이디 설정
        //list_pet에 펫 이름만이라도 데이터베이스에서 가져오려고함





    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            finish()
        }
        return super.onOptionsItemSelected(item)
    }


}