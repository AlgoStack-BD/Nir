package com.algostack.nir.view.frame


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
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
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
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


        binding.linearLayout4.setOnClickListener {

            //findNavController().navigate(R.id.action_home2_to_filter)

            replaceFragmentGenaral(Filter(), Filter::class.java.name)

        }

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

                   val bestForYouResult = result.data.data.filter { it.isApproved && !it.isSold }

                   Log.d("BestForYou",bestForYouResult.toString())
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
        bundle.putString("DestinationPage", "Home")

        replaceFragment(PostDetails(),bundle)
    }


    private fun replaceFragment(fragment: Fragment,bundle: Bundle){
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragment.arguments = bundle
        fragmentTransaction.replace(R.id.fragmentConthainerView4,fragment)

        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()

    }



    private fun replaceFragmentGenaral(fragment: Fragment, flag: String){
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.fragmentConthainerView4,fragment,flag).addToBackStack(flag).commit()

    }








}