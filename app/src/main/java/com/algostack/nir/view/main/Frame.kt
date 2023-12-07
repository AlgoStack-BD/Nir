package com.algostack.nir.view.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.algostack.nir.R
import com.algostack.nir.databinding.ActivityFrameBinding
import com.algostack.nir.view.frame.Chat
import com.algostack.nir.view.frame.Home
import com.algostack.nir.view.frame.Notification
import com.algostack.nir.view.frame.ProfileMenu
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Frame : AppCompatActivity() {

    // Use lateinit to indicate that _binding will be initialized before usage
    private lateinit var _binding: ActivityFrameBinding

    // Check if the binding is initialized before accessing it
    private val binding get() = if (::_binding.isInitialized) _binding else null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFrameBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.bottomNavigation?.background = null
        binding?.bottomNavigation?.menu?.getItem(2)?.isEnabled = false

//        binding?.bottomNavigationView?.menu?.getItem(2)?.isEnabled = false
//        binding?.fab?.setOnClickListener(View.OnClickListener {
//            replaceFragment(add())
//        })


        replaceFragment(Home())

        binding?.bottomNavigation?.setOnItemSelectedListener {

            when(it.itemId){
                R.id.nav_home -> replaceFragment(Home())
                R.id.nav_chat -> replaceFragment(Chat())
                R.id.nav_notification -> replaceFragment(Notification())
                R.id.nav_profile -> replaceFragment(ProfileMenu())

                else -> {

                }

            }
            true
        }



    }


//
//    fun updateBadgeCount(count: Int = 0) {
//        val itemView: BottomNavigationItemView? = binding?.bottomNavigation?.getChildAt(2) as? BottomNavigationItemView
//
//        itemView?.let {
//            // Check if the badge has already been added
//            if (!isBadgeAdded(it)) {
//                // If not added, inflate the badge layout
//                notificationBadge = LayoutInflater.from(this).inflate(R.layout.badge_text, it, false)
//                it.addView(notificationBadge)
//
//                // Now find the TextView inside the badge layout
//                val badgeTextView: TextView = notificationBadge.findViewById(R.id.notificationBadge)
//                badgeTextView.text = count.toString()
//            } else {
//                // If the badge is already added, update the count
//                val badgeTextView: TextView = notificationBadge.findViewById(R.id.notificationBadge)
//                badgeTextView.text = count.toString()
//            }
//        }
//    }
//
//    private fun isBadgeAdded(view: View): Boolean {
//        // Check if the badge is already added
//        for (i in 0 until (view as? ViewGroup)?.childCount!! ?: 0) {
//            val child = (view as? ViewGroup)?.getChildAt(i)
//            if (child === notificationBadge) {
//                return true
//            }
//        }
//        return false
//    }



    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentConthainerView4,fragment)
        fragmentTransaction.commit()
    }
}


