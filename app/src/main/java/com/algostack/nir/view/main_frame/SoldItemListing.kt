package com.algostack.nir.view.main_frame

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.algostack.nir.databinding.FragmentSoldItemListingBinding


class SoldItemListing : Fragment() {

       private var _binding : FragmentSoldItemListingBinding?= null
         private  val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSoldItemListingBinding.inflate(inflater,container,false)
        return binding.root
    }


}