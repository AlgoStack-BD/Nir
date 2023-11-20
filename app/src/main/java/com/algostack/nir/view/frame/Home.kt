package com.algostack.nir.view.frame

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager

import com.algostack.nir.R
import com.algostack.nir.databinding.FragmentHomeBinding
import com.algostack.nir.services.api.PublicPostApi
import com.algostack.nir.utils.AlertDaialog
import com.algostack.nir.utils.AlertDaialog.showCustomAlertDialogBox
import com.algostack.nir.utils.NetworkResult
import com.algostack.nir.view.adapter.PublicFeedBestForYouAdapter
import com.algostack.nir.view.adapter.PublicFeedNearByPostAdapter
import com.algostack.nir.viewmodel.PublicPostViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class Home : Fragment() {

    private var _binding : FragmentHomeBinding ?= null
    private val binding get() = _binding!!

    private val publicPostViewModel by viewModels<PublicPostViewModel> ()

    @Inject
    lateinit var publicPostApi: PublicPostApi

    private lateinit var bestForYouAdapter: PublicFeedBestForYouAdapter
    private lateinit var nearByPostAdapter: PublicFeedNearByPostAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater,container,false)

        bestForYouAdapter = PublicFeedBestForYouAdapter()
        nearByPostAdapter = PublicFeedNearByPostAdapter()


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val selected = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.selected))
        val default = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.white))

        publicPostViewModel.applicationContext = requireContext()
        publicPostViewModel.publicPost()

        binding.home.setOnClickListener {

            ViewCompat.setBackgroundTintList( binding.home, selected)
            ViewCompat.setBackgroundTintList(  binding.appartment, default)
            ViewCompat.setBackgroundTintList(  binding.hotel, default)
            ViewCompat.setBackgroundTintList(  binding.Vila, default)
            ViewCompat.setBackgroundTintList(  binding.cottage, default)

        }
        binding.appartment.setOnClickListener {


            ViewCompat.setBackgroundTintList(  binding.appartment, selected)
            ViewCompat.setBackgroundTintList( binding.home, default)
            ViewCompat.setBackgroundTintList(  binding.hotel, default)
            ViewCompat.setBackgroundTintList(  binding.Vila, default)
            ViewCompat.setBackgroundTintList(  binding.cottage, default)

        }
        binding.hotel.setOnClickListener {

            ViewCompat.setBackgroundTintList( binding.home, default)
            ViewCompat.setBackgroundTintList(  binding.appartment, default)
            ViewCompat.setBackgroundTintList(  binding.hotel, selected)
            ViewCompat.setBackgroundTintList(  binding.Vila, default)
            ViewCompat.setBackgroundTintList(  binding.cottage, default)

        }
        binding.Vila.setOnClickListener {

            ViewCompat.setBackgroundTintList( binding.home, default)
            ViewCompat.setBackgroundTintList(  binding.appartment, default)
            ViewCompat.setBackgroundTintList(  binding.hotel, default)
            ViewCompat.setBackgroundTintList(  binding.Vila, selected)
            ViewCompat.setBackgroundTintList(  binding.cottage, default)

        }
        binding.cottage.setOnClickListener {

            ViewCompat.setBackgroundTintList( binding.home, default)
            ViewCompat.setBackgroundTintList(  binding.appartment, default)
            ViewCompat.setBackgroundTintList(  binding.hotel, default)
            ViewCompat.setBackgroundTintList(  binding.Vila, default)
            ViewCompat.setBackgroundTintList(  binding.cottage, selected)

        }

        binding.beastForYouRecyler.layoutManager = StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL)
        binding.beastForYouRecyler.adapter = bestForYouAdapter

        binding.nearPostRecylerView.layoutManager = StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL)
         binding.nearPostRecylerView.adapter = nearByPostAdapter

//        CoroutineScope(Dispatchers.IO).launch {
//            val response = publicPostApi.getPublicPost()
//            Log.d("PublicAPIrESPONSE",response.body().toString())
//        }


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

}