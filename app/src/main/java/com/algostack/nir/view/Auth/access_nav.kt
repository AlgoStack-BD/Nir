package com.algostack.nir.view.Auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

import com.algostack.nir.R
import com.algostack.nir.databinding.FragmentAccessNavBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class access_nav : Fragment() {

    private  var _binding : FragmentAccessNavBinding ?= null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       _binding = FragmentAccessNavBinding.inflate(inflater,container,false)


        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.accesslogin?.setOnClickListener {

            findNavController().navigate(R.id.action_access_nav_to_signin)
        }


        binding?.accesssignup?.setOnClickListener {

            findNavController().navigate(R.id.action_access_nav_to_signUp)
        }

    }


}