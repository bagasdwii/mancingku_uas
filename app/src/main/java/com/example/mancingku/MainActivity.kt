package com.example.mancingku

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mancingku.adapter.ViewPagerAdapter
import com.example.mancingku.databinding.ActivityMainBinding
import com.example.mancingku.fragment.HomeFragment
import com.example.mancingku.fragment.MapsFragment
import com.example.mancingku.fragment.PostFragment
import com.example.mancingku.fragment.UserFragment

class MainActivity : AppCompatActivity() {
    lateinit var binding :ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupTab()
    }

    private fun setupTab() {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(HomeFragment(),"")
        adapter.addFragment(MapsFragment(),"")
        adapter.addFragment(PostFragment(),"")
        adapter.addFragment(UserFragment(),"")


        binding.viewPager.adapter = adapter
        binding.tabs.setupWithViewPager(binding.viewPager)

        binding.tabs.getTabAt(0)!!.setIcon(R.drawable.ic_home)
        binding.tabs.getTabAt(1)!!.setIcon(R.drawable.ic_map)
        binding.tabs.getTabAt(2)!!.setIcon(R.drawable.ic_add)
        binding.tabs.getTabAt(3)!!.setIcon(R.drawable.ic_account)
    }
}