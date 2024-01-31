package com.algostack.nir.view.frame

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.algostack.nir.R
import com.algostack.nir.databinding.FragmentFilterBinding
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.card.MaterialCardView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.slider.Slider


class Filter : Fragment() {

     private var _binding : FragmentFilterBinding ?= null
        private val binding get() = _binding!!
    private var selectedCard: MaterialCardView? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFilterBinding.inflate(inflater,container,false)

        setupBackPress()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




            binding.pricerangeSlider.addOnChangeListener { slider, value, fromUser ->

                binding.endRange.setText( value.toInt().toString())

        }


        if (arguments?.getString("cityName") == null){
            binding.locationText.text = "Sylhet"
        }else{
            binding.locationText.text = arguments?.getString("cityName")
        }


        binding.LocationPicker.setOnClickListener {

            replaceFragment(SelectCity())

        }


        setCardClickListener(binding.numberOfOneBedroom, 1)
        setCardClickListener(binding.numberOfTwoBedroom, 2)
        setCardClickListener(binding.numberOfThreeBedroom, 3)
        setCardClickListener(binding.numberOfFourBedroom, 4)
        setCardClickListener(binding.numberOfFiveBedroom, 5)
        setCardClickListener(binding.numberOfSixBedroom, 6)

    }



    private fun setCardClickListener(card: MaterialCardView, numberOfBedrooms: Int? = null) {
        card.setOnClickListener {
            if (selectedCard == null) {
                // No card is selected, so select the clicked card
                selectCard(card)
            } else if (selectedCard == card) {
                // The clicked card is already selected, so deselect it
                deselectCard(card)
            } else {
                // Another card is selected, deselect the currently selected card and select the clicked card
                deselectCard(selectedCard!!)
                selectCard(card, numberOfBedrooms)
            }
        }
    }

    private fun selectCard(card: MaterialCardView, numberOfBedrooms: Int? = null) {
        selectedCard = card
        Toast.makeText(requireContext(), "Number of bedrooms: $numberOfBedrooms", Toast.LENGTH_SHORT).show()
        card.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.selected))
    }

    private fun deselectCard(card: MaterialCardView) {
        selectedCard = null
        card.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
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


    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentConthainerView4,fragment)

        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
        val navBar = activity?.findViewById<BottomAppBar>(R.id.bottomAppBar)
        val flotBar = activity?.findViewById<FloatingActionButton>(R.id.fab)
        navBar?.isVisible = false
        flotBar?.isVisible = false
    }


}