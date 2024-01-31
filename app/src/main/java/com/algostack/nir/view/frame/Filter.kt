package com.algostack.nir.view.frame

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.log


@AndroidEntryPoint
class Filter : Fragment() {

     private var _binding : FragmentFilterBinding ?= null
        private val binding get() = _binding!!
    private var selectedCard: MaterialCardView? = null
    private var selectedCard2: MaterialCardView? = null
    private var selectedBedRoom: Int? = null
    private var selectedBathRoom: Int? = null
    private var minmumPriceRange = 0
    private var maximumPriceRange = 0
    private var LocationArea = ""
    private var numberOfBedRoom = 0
    private var numberOfBathRoom = 0
    private var selectedRentType: String? = null
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



        if (binding.startRange.text.toString().isEmpty()){
             minmumPriceRange = 0
        }else{
           minmumPriceRange = binding.startRange.text.toString().toInt()
        }
        if (binding.endRange.text.toString().isEmpty()){
            maximumPriceRange = 0
        }else{
            maximumPriceRange = binding.endRange.text.toString().toInt()
        }

        if (binding.locationText.text.toString().isEmpty()){
            LocationArea = ""
        }else{
            LocationArea = binding.locationText.text.toString()
        }
        if (selectedBedRoom == null){
            numberOfBedRoom = 0
        }else{
            numberOfBedRoom = selectedBedRoom!!
        }

        if (selectedBathRoom == null){
            numberOfBathRoom = 0
        }else{
            numberOfBathRoom = selectedBathRoom!!
        }

        if (arguments?.getString("cityName") == null){
            binding.locationText.text = "Sylhet"
        }else{
            binding.locationText.text = arguments?.getString("cityName")
        }


        binding.LocationPicker.setOnClickListener {

            replaceFragment(SelectCity())
          //  findNavController().navigate(R.id.action_filter_to_selectCity)

        }


        // Card item selected listener


        setCardClickListener(binding.numberOfOneBedroom, 1 , 201)
        setCardClickListener(binding.numberOfTwoBedroom, 2 ,201)
        setCardClickListener(binding.numberOfThreeBedroom, 3,201)
        setCardClickListener(binding.numberOfFourBedroom, 4,201)
        setCardClickListener(binding.numberOfFiveBedroom, 5,201)
        setCardClickListener(binding.numberOfSixBedroom, 6,201)

        setCardClickListener(binding.numberOfOneBathroom, 1,202)
        setCardClickListener(binding.numberOfTwoBathroom, 2,202)
        setCardClickListener(binding.numberOfThreeBathroom, 3,202)
        setCardClickListener(binding.numberOfFourBathroom, 4,202)
        setCardClickListener(binding.numberOfFiveBathroom, 5,202)
        setCardClickListener(binding.numberOfSixBathroom, 6,202)


        // spiner for rent type
        binding.renttypespinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    binding.renttypespinner.setSelection(0)
                }

                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    binding.renttypespinner.setSelection(position)


                    selectedRentType = adapterView?.getItemAtPosition(position).toString()


                }

            }

        // Apply button click listener
        binding.filterContinue.setOnClickListener {

        }


    }





   private fun setCardClickListener(card: MaterialCardView, numberOfBedrooms: Int? = null, code: Int? = null) {

       card.setOnClickListener {
           CoroutineScope(Dispatchers.Main).launch {
               if (code == 201) {
                   if (selectedCard == null) {
                       // No card is selected, so select the clicked card
                       selectCard(card, numberOfBedrooms, code)
                   } else if (selectedCard == card) {
                       // The clicked card is already selected, so deselect it
                       deselectCard(card, code)
                          selectedBedRoom = null
                   } else {
                       // Another card is selected, deselect the currently selected card and select the clicked card
                       deselectCard(selectedCard!!)
                       selectCard(card, numberOfBedrooms, code)
                   }
               }

               if (code == 202) {
                   if (selectedCard2 == null) {
                       // No card is selected, so select the clicked card
                       selectCard(card, numberOfBedrooms, code)
                   } else if (selectedCard2 == card) {
                       // The clicked card is already selected, so deselect it
                       selectedBathRoom = null
                       deselectCard(card, code)
                   } else {
                       // Another card is selected, deselect the currently selected card and select the clicked card
                       deselectCard(selectedCard2!!)
                       selectCard(card, numberOfBedrooms, code)
                   }
               }
           }

        }
    }

    private suspend fun selectCard(card: MaterialCardView, numberOfBedrooms: Int? = null, code: Int? = null) {
        withContext(Dispatchers.Main) {

            if (code == 201) {
                selectedCard = card
                selectedBedRoom = numberOfBedrooms
                println("selectedBedRoom $selectedBedRoom  , selectedBathRoom: $selectedBathRoom" )
            } else if (code == 202) {
                selectedCard2 = card
                selectedBathRoom = numberOfBedrooms
                println("selectedBedRoom $selectedBedRoom  , selectedBathRoom: $selectedBathRoom" )
            }
            card.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.selected))
        }
    }

    private suspend fun deselectCard(card: MaterialCardView, code: Int? = null) {
        withContext(Dispatchers.Main) {
            if (code == 201) {
                selectedCard = null
            } else if (code == 202) {
                selectedCard2 = null
            }

            card.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
        }
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
        fragmentTransaction.addToBackStack("filter")
        fragmentTransaction.commit()
    }


}