package com.algostack.nir.view.Splash

import android.content.Intent
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
import com.algostack.nir.utils.TokenManager
import com.algostack.nir.view.main.Frame
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
@Suppress
class Splash_screen : Fragment() {

    private var _binding: FragmentSplashScreenBinding ?= null

    private  val binding get() = _binding

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
      _binding = FragmentSplashScreenBinding.inflate(inflater,container,false)

        Handler(Looper.getMainLooper()).postDelayed({

            if (tokenManager.getToken() != null){
                val intent = Intent(activity, Frame::class.java)
                activity?.startActivity(intent)
            }else
            {
                findNavController().navigate(R.id.action_splash_screen_to_access_nav)
            }

           // findNavController().navigate(R.id.action_splash_screen_to_access_nav)

        }, 3000)

        return binding?.root
    }


}