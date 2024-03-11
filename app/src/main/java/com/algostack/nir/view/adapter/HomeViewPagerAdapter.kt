package com.algostack.nir.view.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.algostack.nir.view.main_frame.Appartment
import com.algostack.nir.view.main_frame.Cottage
import com.algostack.nir.view.main_frame.MainHome
import com.algostack.nir.view.main_frame.Vila

class HomeViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment){



    override fun getItemCount() : Int {
        return 4
    }

    override fun createFragment(p0: Int): Fragment {
        return when(p0){
            0 -> MainHome()
            1 -> Vila()
            2 -> Appartment()
            else -> Cottage()
        }
    }

}