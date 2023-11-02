package com.algostack.nir.view.Splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

import com.algostack.nir.R
import com.algostack.nir.databinding.FragmentSplashScreenBinding

@Suppress
class Splash_screen : Fragment() {

    private var _binding: FragmentSplashScreenBinding ?= null

    private  val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
      _binding = FragmentSplashScreenBinding.inflate(inflater,container,false)

        Handler(Looper.getMainLooper()).postDelayed({
            findNavController().navigate(R.id.action_splash_screen_to_access_nav)
        }, 3000)

        return binding?.root
    }


}