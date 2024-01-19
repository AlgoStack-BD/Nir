package com.algostack.nir.view.frame

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.algostack.nir.R
import com.algostack.nir.databinding.FragmentHomeBinding
import com.algostack.nir.services.api.PublicPostApi
import com.algostack.nir.services.model.PublicPostData
import com.algostack.nir.utils.AlertDaialog.showCustomAlertDialogBox
import com.algostack.nir.utils.NetworkResult
import com.algostack.nir.view.adapter.HorizontalSpace
import com.algostack.nir.view.adapter.VerticalSpace
import com.algostack.nir.view.adapter.PublicFeedBestForYouAdapter
import com.algostack.nir.view.adapter.PublicFeedNearByPostAdapter
import com.algostack.nir.viewmodel.PublicPostViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Home : Fragment() {

    private var _binding : FragmentHomeBinding ?= null
    private val binding get() = _binding!!

    private val publicPostViewModel by viewModels<PublicPostViewModel> ()

    @Inject
    lateinit var publicPostApi: PublicPostApi

    val bestForYouRecSpace = VerticalSpace()
    val nearRecSpace = HorizontalSpace()


    private lateinit var bestForYouAdapter: PublicFeedBestForYouAdapter
    private lateinit var nearByPostAdapter: PublicFeedNearByPostAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater,container,false)


        bestForYouAdapter = PublicFeedBestForYouAdapter(this::onDetailsCliked)
        nearByPostAdapter = PublicFeedNearByPostAdapter()


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        val selected = ContextCompat.getDrawable(requireContext(), R.drawable.buttonclickedbackground);
        val default = ContextCompat.getDrawable(requireContext(), R.drawable.horizontal_button_circle);
        val selectedColour = ContextCompat.getColor(requireContext(), R.color.white)
        val defaultColour = ContextCompat.getColor(requireContext(), R.color.colorSecendaryBlack)

        publicPostViewModel.applicationContext = requireContext()
        publicPostViewModel.publicPost()
        // Setup sticky header
       
        binding.home.setOnClickListener {

            ViewCompat.setBackground( binding.home, selected)
            ViewCompat.setBackground(  binding.appartment, default)
            ViewCompat.setBackground(  binding.hotel, default)
            ViewCompat.setBackground(  binding.Vila, default)
            ViewCompat.setBackground(  binding.cottage, default)


            binding.HomeBtnText.setTextColor(selectedColour)
            binding.appartmentBtnText.setTextColor(defaultColour)
            binding.HotelBtnText.setTextColor(defaultColour)
            binding.VilaBtnText.setTextColor(defaultColour)
            binding.CottageBtnText.setTextColor(defaultColour)


        }
        binding.appartment.setOnClickListener {


            ViewCompat.setBackground(  binding.appartment, selected)
            ViewCompat.setBackground( binding.home, default)
            ViewCompat.setBackground(  binding.hotel, default)
            ViewCompat.setBackground(  binding.Vila, default)
            ViewCompat.setBackground(  binding.cottage, default)

            binding.HomeBtnText.setTextColor(defaultColour)
            binding.appartmentBtnText.setTextColor(selectedColour)
            binding.HotelBtnText.setTextColor(defaultColour)
            binding.VilaBtnText.setTextColor(defaultColour)
            binding.CottageBtnText.setTextColor(defaultColour)

        }
        binding.hotel.setOnClickListener {

            ViewCompat.setBackground( binding.home, default)
            ViewCompat.setBackground(  binding.appartment, default)
            ViewCompat.setBackground(  binding.hotel, selected)
            ViewCompat.setBackground(  binding.Vila, default)
            ViewCompat.setBackground(  binding.cottage, default)


            binding.HomeBtnText.setTextColor(defaultColour)
            binding.appartmentBtnText.setTextColor(defaultColour)
            binding.HotelBtnText.setTextColor(selectedColour)
            binding.VilaBtnText.setTextColor(defaultColour)
            binding.CottageBtnText.setTextColor(defaultColour)

        }
        binding.Vila.setOnClickListener {

            ViewCompat.setBackground( binding.home, default)
            ViewCompat.setBackground(  binding.appartment, default)
            ViewCompat.setBackground(  binding.hotel, default)
            ViewCompat.setBackground(  binding.Vila, selected)
            ViewCompat.setBackground(  binding.cottage, default)

            binding.HomeBtnText.setTextColor(defaultColour)
            binding.appartmentBtnText.setTextColor(defaultColour)
            binding.HotelBtnText.setTextColor(defaultColour)
            binding.VilaBtnText.setTextColor(selectedColour)
            binding.CottageBtnText.setTextColor(defaultColour)

        }
        binding.cottage.setOnClickListener {

            ViewCompat.setBackground( binding.home, default)
            ViewCompat.setBackground(  binding.appartment, default)
            ViewCompat.setBackground(  binding.hotel, default)
            ViewCompat.setBackground(  binding.Vila, default)
            ViewCompat.setBackground(  binding.cottage, selected)

            binding.HomeBtnText.setTextColor(defaultColour)
            binding.appartmentBtnText.setTextColor(defaultColour)
            binding.HotelBtnText.setTextColor(defaultColour)
            binding.VilaBtnText.setTextColor(defaultColour)
            binding.CottageBtnText.setTextColor(selectedColour)

        }


        binding.bestForYouRecylerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        binding.bestForYouRecylerView.addItemDecoration(bestForYouRecSpace)
        binding.bestForYouRecylerView.adapter = bestForYouAdapter


        binding.nearfromyouRecyler.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        binding.nearfromyouRecyler.addItemDecoration(nearRecSpace)
         binding.nearfromyouRecyler.adapter = nearByPostAdapter




        bindOvservers()


    }

    private fun bindOvservers() {
       publicPostViewModel.publicPostResponseLiveData.observe(viewLifecycleOwner, Observer {result ->
        //   binding?.logprogressBar?.isVisible = false
       when(result){

           is NetworkResult.Success -> {
               if(result.data!!.status == 200){

                   val bestForYouResult = result.data.data

                   bestForYouAdapter.submitList(bestForYouResult)
                   nearByPostAdapter.submitList(bestForYouResult)


               }else if(result.data.status == 500){

                   result.message?.let { it1 -> showCustomAlertDialogBox(requireContext(),it1) }
               }
           }
           is NetworkResult.Error -> {
               showCustomAlertDialogBox(requireContext() , result.message ?: "Something went wrong")
           }
           is NetworkResult.Loading -> {
            //   binding?.logprogressBar?.isVisible = true
           }



       }



       })


    }


    private fun onDetailsCliked(publicPostData: PublicPostData) {
        val bundle = Bundle()
        bundle.putString("details", Gson().toJson(publicPostData))

//        bundle.putString("UserName",Gson().toJson(publicPostData.userName))
//        bundle.putString("UserImg",Gson().toJson(publicPostData.userImg))
//        bundle.putString("PostImg",Gson().toJson(publicPostData.img))
//        bundle.putString("additionalmsg",Gson().toJson(publicPostData.additionalMessage))
//        bundle.putString("price",Gson().toJson(publicPostData.price))
//        bundle.putString("bedroom",Gson().toJson(publicPostData.bedRoom))
//        bundle.putString("bathroom",Gson().toJson(publicPostData.bathRoom))
//        bundle.putString("Phone",Gson().toJson(publicPostData.phone))
//        bundle.putString("Type",Gson().toJson(publicPostData.type))
//        bundle.putString("Address", Gson().toJson(publicPostData.location))
//        bundle.putString("gassBill", Gson().toJson(publicPostData.bills.gasBill))
//        bundle.putString("WaterBill", Gson().toJson(publicPostData.bills.waterBill))
//        bundle.putString("ElictrictBill", Gson().toJson(publicPostData.bills.electricBill))
//        bundle.putString("Belcony", Gson().toJson(publicPostData.balcony))
//        bundle.putString("DiningRoom", Gson().toJson(publicPostData.diningRoom))
//        bundle.putString("Kitchen", Gson().toJson(publicPostData.kitchen))
//        bundle.putString("LikeCouont", Gson().toJson(publicPostData.likeCount))
//        bundle.putString("RentPrice", Gson().toJson(publicPostData.price))
//        bundle.putString("isNegotiable", Gson().toJson(publicPostData.isNegotiable))

     //   findNavController().navigate(R.id.action_home2_to_postDetails, bundle)
        replaceFragment(PostDetails(),bundle)
    }


    private fun replaceFragment(fragment: Fragment,bundle: Bundle){
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragment.arguments = bundle
        fragmentTransaction.replace(R.id.fragmentConthainerView4,fragment)
        fragmentTransaction.commit()
    }







}