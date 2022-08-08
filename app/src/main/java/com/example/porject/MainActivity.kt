package com.example.porject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.porject.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    lateinit var binding: MainActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val list = listOf(mapView(), myPet(), Diet(), myPage())
        val pagerAdapter = FragmentPagerAdapter(list, this)
        binding.viewPager.adapter = pagerAdapter
        val titles = listOf("산책", "마이 펫", "일기", "마이페이지")
        TabLayoutMediator(binding.tabLayout, binding.viewPager){tab, position -> tab.text = titles.get(position)}.attach()
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
