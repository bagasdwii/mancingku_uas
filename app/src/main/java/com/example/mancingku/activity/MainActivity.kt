package com.example.mancingku.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import com.example.mancingku.R


class MainActivity : AppCompatActivity() {
//    private lateinit var drawerLayout: DrawerLayout
//    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


//        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
//        navController=navHostFragment.navController
//        val bottomNavigationView=findViewById<BottomNavigationView>(R.id.)
//        setupActionBarWithNavController()

//        @Suppress("UNUSED_VARIABLE")
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        val navController = this.findNavController(R.id.nav_host_fragment)
//
//        NavigationUI.setupActionBarWithNavController(this,navController, drawerLayout)
//
//        NavigationUI.setupWithNavController(binding.fragmentContainer, navController)
        // Check if a specific fragment needs to be loaded
    }
//    val fragmentHome: Fragment = HomeFragment()
//    val fragmentMaps: Fragment = MapsFragment()
//    val fragmentPost: Fragment = PostFragment()
//    val fragmentUser: Fragment = UserFragment()
//    val fm: FragmentManager = supportFragmentManager
//    var active: Fragment = fragmentHome
//
//    private lateinit var menu: Menu
//    private lateinit var menuItem: MenuItem
//    private lateinit var bottomNavigationView: BottomNavigationView
//
//    private lateinit var binding: ActivityMainBinding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        setUpBottomNav()
//    }
//
//    private fun setUpBottomNav() {
//        fm.beginTransaction().add(R.id.container, fragmentHome).show(fragmentHome).commit()
//        fm.beginTransaction().add(R.id.container, fragmentMaps).hide(fragmentMaps).commit()
//        fm.beginTransaction().add(R.id.container, fragmentPost).hide(fragmentPost).commit()
//        fm.beginTransaction().add(R.id.container, fragmentUser).hide(fragmentUser).commit()
//
//        bottomNavigationView = binding.navView
//        menu = bottomNavigationView.menu
//        menuItem = menu.getItem(0)
//        menuItem.isChecked = true
//
//        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
//
//            when (item.itemId) {
//                R.id.navigation_home -> {
//                    callFragment(0, fragmentHome)
//                }
//
//                R.id.navigation_maps -> {
//                    callFragment(1, fragmentMaps)
//                }
//
//                R.id.navigation_post -> {
//                    callFragment(2, fragmentPost)
//                }
//
//                R.id.navigation_user -> {
//                callFragment(3, fragmentUser)
//                }
//            }
//            false
//        }
//    }
//
//    private fun callFragment(index: Int, fragment: Fragment) {
//        menuItem = menu.getItem(index)
//        menuItem.isChecked = true
//        fm.beginTransaction().hide(active).show(fragment).commit()
//        active = fragment
//    }
}