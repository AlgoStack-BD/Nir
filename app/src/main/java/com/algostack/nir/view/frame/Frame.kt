package com.algostack.nir.view.frame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.algostack.nir.R
import com.algostack.nir.databinding.ActivityFrameBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Frame : AppCompatActivity() {

    private var _binding: ActivityFrameBinding ?= null
    private val binding get() = _binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFrameBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.bottomNavigationView?.menu?.getItem(2)?.isEnabled = false
        binding?.fab?.setOnClickListener(View.OnClickListener {
            replaceFragment(add())
        })


        replaceFragment(Home())

        binding?.bottomNavigationView?.setOnItemSelectedListener {

            when(it.itemId){
                R.id.nav_home -> replaceFragment(Home())
                R.id.nav_fav -> replaceFragment(Favorite())
                R.id.nav_profile -> replaceFragment(Profile())

                else -> {

                }

            }
            true
        }



    }


    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentConthainerView4,fragment)
        fragmentTransaction.commit()
    }
}