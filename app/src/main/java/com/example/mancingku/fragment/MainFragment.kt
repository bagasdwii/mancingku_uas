package com.example.mancingku.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.FragmentManager
import com.example.mancingku.R
import com.example.mancingku.databinding.ActivityMainBinding
import com.example.mancingku.databinding.FragmentMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var bottomNavigationView: BottomNavigationView

    private val fragmentHome: Fragment = HomeFragment()
    private val fragmentMaps: Fragment = MapsFragment()
    private val fragmentPost: Fragment = PostFragment()
    private val fragmentUser: Fragment = UserFragment()
    private var active: Fragment = fragmentHome

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpBottomNav()
    }

    private fun setUpBottomNav() {
        childFragmentManager.beginTransaction()
            .add(R.id.container, fragmentHome)
            .add(R.id.container, fragmentMaps)
            .add(R.id.container, fragmentPost)
            .add(R.id.container, fragmentUser)
            .hide(fragmentMaps)
            .hide(fragmentPost)
            .hide(fragmentUser)
            .show(fragmentHome)
            .commit()

        bottomNavigationView = binding.navView
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> callFragment(fragmentHome)
                R.id.navigation_maps -> callFragment(fragmentMaps)
                R.id.navigation_post -> callFragment(fragmentPost)
                R.id.navigation_user -> callFragment(fragmentUser)
            }
            true
        }
    }

    private fun callFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .hide(active)
            .show(fragment)
            .commit()
        active = fragment
    }
}

