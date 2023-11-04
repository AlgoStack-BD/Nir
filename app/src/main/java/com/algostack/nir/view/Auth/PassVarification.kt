package com.algostack.nir.view.Auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

import com.algostack.nir.R
import com.algostack.nir.databinding.FragmentPassVarificationBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PassVarification : Fragment() {

    private var _binding : FragmentPassVarificationBinding ?= null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPassVarificationBinding.inflate(inflater,container,false)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.otpContinue?.setOnClickListener {
            findNavController().navigate(R.id.action_passVarification_to_newPassword)
        }
    }


}