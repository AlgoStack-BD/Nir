package com.algostack.nir.view.frame

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import com.algostack.nir.R
import com.algostack.nir.databinding.FragmentAddBinding
import dagger.hilt.android.AndroidEntryPoint
import hilt_aggregated_deps._com_algostack_nir_viewmodel_AuthViewModel_HiltModules_BindsModule

@AndroidEntryPoint
class add : Fragment() {

    private var _binding : FragmentAddBinding ?= null
    private val binding get() = _binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddBinding.inflate(inflater,container,false)
        return binding?.root
    }


}