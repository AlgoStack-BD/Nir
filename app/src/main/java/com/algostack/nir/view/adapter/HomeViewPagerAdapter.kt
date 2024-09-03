package com.algostack.nir.view.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.algostack.nir.view.frame.Appartment
import com.algostack.nir.view.frame.Category
import com.algostack.nir.view.frame.Cottage
import com.algostack.nir.view.frame.FavouriteItem
import com.algostack.nir.view.frame.MainHome
import com.algostack.nir.view.frame.MyListings
import com.algostack.nir.view.frame.SoldItemListing
import com.algostack.nir.view.frame.Vila

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