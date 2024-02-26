package com.algostack.nir.view.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.algostack.nir.R
import com.algostack.nir.databinding.ActivityFrameBinding
import com.algostack.nir.view.frame.Category
import com.algostack.nir.view.frame.Chat
import com.algostack.nir.view.frame.Home
import com.algostack.nir.view.frame.Notification
import com.algostack.nir.view.frame.ProfileMenu
import com.algostack.nir.view.frame.add
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Frame : AppCompatActivity() {

    // Use lateinit to indicate that _binding will be initialized before usage
    private lateinit var _binding: ActivityFrameBinding


    // Check if the binding is initialized before accessing it
    private val binding get() = if (::_binding.isInitialized) _binding else null

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFrameBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.bottomNavigation?.background = null
        binding?.bottomNavigation?.menu?.getItem(2)?.isEnabled = false

        binding?.bottomNavigation?.menu?.getItem(2)?.isEnabled = false
        binding?.fab?.setOnClickListener(View.OnClickListener {
            replaceFragment(add())
        })


        replaceFragment(Home())

        binding?.bottomNavigation?.setOnItemSelectedListener {

            when(it.itemId){
                R.id.nav_home2 -> replaceFragment(Home())
                R.id.nav_chat -> replaceFragment(Category())
                R.id.nav_notification -> replaceFragment(Notification())
                R.id.nav_profileMenu -> replaceFragment(ProfileMenu())


                else -> {

                }

            }
            true
        }



    }


    override fun onSupportNavigateUp(): Boolean {
        navController = findNavController(R.id.fragmentConthainerView4)
        return  navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentConthainerView4,fragment)

        fragmentTransaction.commit()
    }
}


