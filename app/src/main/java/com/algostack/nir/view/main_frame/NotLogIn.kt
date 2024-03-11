package com.algostack.nir.view.main_frame

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.algostack.nir.R
import com.algostack.nir.databinding.FragmentNotLogInBinding
import com.algostack.nir.view.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotLogIn : Fragment() {

    private var chekDestinationPage = ""
    private var _binding: FragmentNotLogInBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentNotLogInBinding.inflate(inflater, container, false)
         setupBackPress()
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val destinationPage = arguments?.getString("DestinationPage")
        if (destinationPage != null) {
            chekDestinationPage = destinationPage

            binding.setTitle.text = chekDestinationPage
        }

        binding.logincommand.setOnClickListener {
            val intent = Intent(activity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            activity?.startActivity(intent)
        }
    }

    private fun setupBackPress() {
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Handle the back button event
                if(isEnabled){
                    val fragmentManager = parentFragmentManager
                    val fragmentTransaction = fragmentManager.beginTransaction()

                    if (chekDestinationPage == "Home"){
                        fragmentTransaction.replace(R.id.fragmentConthainerView4,Home())
                        fragmentTransaction.remove(this@NotLogIn)
                    }else if (chekDestinationPage == "Notification"){
                        fragmentTransaction.replace(R.id.fragmentConthainerView4,Notification())
                        fragmentTransaction.remove(this@NotLogIn)
                    }else if (chekDestinationPage == "ProfileDetails") {
                        fragmentTransaction.replace(R.id.fragmentConthainerView4, ProfileDetails())
                        fragmentTransaction.remove(this@NotLogIn)
                    }



                    fragmentTransaction.commit()
                }


            }
        }
        )
    }


}