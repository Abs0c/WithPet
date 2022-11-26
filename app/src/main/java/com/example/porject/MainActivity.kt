package com.example.porject

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.porject.databinding.ActivityMainBinding
import com.example.porject.databinding.FragmentMyPetBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    lateinit var binding: MainActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle("반려동물 관리어플")
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //val list = listOf(mapView(), myPet(), Diary(), myPage())
        val list = listOf(mapView(), Diary(), CommunityView(),myPage())
        val pagerAdapter = FragmentPagerAdapter(list, this)
        binding.viewPager.adapter = pagerAdapter
        val titles = listOf("산책", "일기", "게시판", "마이페이지")
        TabLayoutMediator(binding.tabLayout, binding.viewPager){tab, position -> tab.text = titles.get(position)}.attach()
        binding.viewPager.isUserInputEnabled = false
    }
    override fun onRestart() {
        super.onRestart()
        myPet().refresh(myPet(), supportFragmentManager)
    }
    var time : Long = 0
    override fun onBackPressed() {
        if(System.currentTimeMillis() - time < 2000){
            finishAffinity()
            return
        }
        else {
            Toast.makeText(this, "한 번 더 누르면 종료합니다.", Toast.LENGTH_SHORT).show()
            time = System.currentTimeMillis()
        }
    }

}

class FragmentPagerAdapter(val fragmentList:List<Fragment>, fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity){
    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList.get(position)
    }
}
