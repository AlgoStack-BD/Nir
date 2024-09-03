package com.algostack.nir.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.algostack.nir.view.frame.FavouriteItem
import com.algostack.nir.view.frame.MyListings
import com.algostack.nir.view.frame.SoldItemListing

class ProfileViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment){

   var fragmentList : ArrayList<Fragment> = ArrayList()
    var fragmentTitleList : ArrayList<String> = ArrayList()





//    fun addFragment(fragment: Fragment, title: String) {
//        fragmentList.add(fragment)
//        fragmentTitleList.add(title)
//    }

    override fun getItemCount() : Int {
        return 3
    }

    override fun createFragment(p0: Int): Fragment {
        return when(p0){
            0 -> MyListings()
            1 -> FavouriteItem()
            else -> SoldItemListing()
        }
    }

}