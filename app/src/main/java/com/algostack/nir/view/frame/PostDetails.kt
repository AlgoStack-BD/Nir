package com.algostack.nir.view.frame

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.algostack.nir.R
import com.algostack.nir.databinding.FragmentPostDetailsBinding
import com.algostack.nir.services.model.ImageItem
import com.algostack.nir.services.model.PublicPostData
import com.algostack.nir.utils.TokenManager
import com.algostack.nir.view.adapter.ImageDetailsSmallViewAdapter
import com.bumptech.glide.Glide
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import java.util.UUID
import javax.inject.Inject

@AndroidEntryPoint
class PostDetails : Fragment() {

    private var _binding: FragmentPostDetailsBinding? = null
    private val binding get() = _binding!!
    val newImageArray = arrayListOf<ImageItem>()

    @Inject
   lateinit var tokenManager: TokenManager

    private var detailsData: PublicPostData? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPostDetailsBinding.inflate(inflater, container, false)

        setupBackPress()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setInialData()

        Glide
            .with(requireContext())
            .load(tokenManager.getUserImage())
            .centerCrop()
            .placeholder(R.drawable.profile)
            .into(binding.userProfile)
        binding.ownerName.text = tokenManager.getUserName()

        val imageAdapter = ImageDetailsSmallViewAdapter()
         binding.imageRV.adapter = imageAdapter
        imageAdapter.submitList(newImageArray)


    }

    private fun setInialData() {
        val jsonDetails = arguments?.getString("details")
        if (jsonDetails != null) {
            detailsData = Gson().fromJson(jsonDetails, PublicPostData::class.java)

            detailsData.let { it ->
                binding.txtOwnerName.text = it?.userName

                // println("ChekLink: "+it!!.img)
                val imagArray: List<String> = it!!.img.split(",")    // split the string
                imagArray.forEach {
                    newImageArray.add(ImageItem(UUID.randomUUID().toString(), it))
                }
                binding.txtDescription.text = it!!.additionalMessage
                binding.location.text = it!!.location
                binding.numOfBadroom.text = it!!.bedRoom.toString()
                binding.numOfBbathroom.text = it!!.bathRoom.toString()
                binding.numOfKitchen.text = it!!.kitchen.toString()
                binding.numOfDinigroom.text = it!!.diningRoom.toString()
                binding.numOfDrawing.text = it!!.drawingRoom.toString()
                binding.numOfBelcony.text = it!!.balcony.toString()
                binding.rentPriceAmmount.text = "${it!!.price.toString()} ৳"
                binding.electricityBillChekboox.isChecked = it!!.bills.electricBill
                binding.checkbocgass.isChecked = it!!.bills.gasBill
                if(it!!.isNegotiable){
                    binding.isNagotiable.visibility = View.VISIBLE
                    binding.fixed.visibility = View.GONE
                    binding.isNagotiable.isChecked = true
                }else
                {
                    binding.isNagotiable.visibility = View.GONE
                    binding.fixed.visibility = View.VISIBLE
                    binding.fixed.isChecked = true
                }


            }
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




}