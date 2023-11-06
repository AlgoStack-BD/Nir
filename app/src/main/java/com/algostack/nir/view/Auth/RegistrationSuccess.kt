package com.algostack.nir.view.Auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.algostack.nir.databinding.FragmentRegistrationSuccessBinding
import com.algostack.nir.view.frame.Frame


class RegistrationSuccess : Fragment() {

   private var _binding : FragmentRegistrationSuccessBinding ?= null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding  = FragmentRegistrationSuccessBinding.inflate(inflater,container,false)

        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.goHome?.setOnClickListener {
            val intent = Intent(getActivity(), Frame::class.java)
            getActivity()?.startActivity(intent)

        }

    }

}