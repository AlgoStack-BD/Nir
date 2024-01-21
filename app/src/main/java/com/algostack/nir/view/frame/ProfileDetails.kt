package com.algostack.nir.view.frame

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.algostack.nir.R
import com.algostack.nir.databinding.FragmentProfileDetailsBinding
import com.algostack.nir.utils.TokenManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileDetails : Fragment() {

    private var _binding : FragmentProfileDetailsBinding ?= null
    private val binding get() = _binding!!

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentProfileDetailsBinding.inflate(inflater,container,false)

        setupBackPress()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val selected = ContextCompat.getDrawable(requireContext(), R.drawable.buttonclickedbackground);
        val default = ContextCompat.getDrawable(requireContext(), R.drawable.horizontal_button_circle);
        val selectedColour = ContextCompat.getColor(requireContext(), R.color.white)
        val defaultColour = ContextCompat.getColor(requireContext(), R.color.colorSecendaryBlack)

        binding.profileMyList.setOnClickListener {

            ViewCompat.setBackground( binding.profileMyList, selected)
            ViewCompat.setBackground(  binding.profilefvrt, default)

            binding.mylist.setTextColor(selectedColour)
            binding.fvrtsection.setTextColor(defaultColour)

        }

        binding.profilefvrt.setOnClickListener {

                ViewCompat.setBackground( binding.profileMyList, default)
                ViewCompat.setBackground(  binding.profilefvrt, selected)

                binding.mylist.setTextColor(defaultColour)
                binding.fvrtsection.setTextColor(selectedColour)
        }



        Glide
            .with(requireContext())
            .load(tokenManager.getUserImage())
            .centerCrop()
            .placeholder(R.drawable.profile)
            .into(binding.profileimg)

        binding.userName.text = tokenManager.getUserName()
        binding.userEmail.text = tokenManager.getUserEmail()


    }

    private fun setupBackPress() {
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Handle the back button event
                if(isEnabled){
                    val navBar = activity?.findViewById<BottomAppBar>(R.id.bottomAppBar)
                    val flotBar = activity?.findViewById<FloatingActionButton>(R.id.fab)
                    navBar?.isVisible = true
                    flotBar?.isVisible = true

                    isEnabled = false
                    requireActivity().onBackPressed()
                }


            }
        }
        )
    }


}