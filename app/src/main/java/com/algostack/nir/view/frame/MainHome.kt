package com.algostack.nir.view.frame

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.algostack.nir.R
import com.algostack.nir.databinding.FragmentMainHomeBinding
import com.algostack.nir.services.model.PublicPostData
import com.algostack.nir.utils.AlertDaialog
import com.algostack.nir.utils.NetworkResult
import com.algostack.nir.utils.TokenManager
import com.algostack.nir.view.adapter.HorizontalSpace
import com.algostack.nir.view.adapter.PublicFeedBestForYouAdapter
import com.algostack.nir.view.adapter.PublicFeedNearByPostAdapter
import com.algostack.nir.view.adapter.VerticalSpace
import com.algostack.nir.viewmodel.PublicPostViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainHome : Fragment() {


    private var _binding: FragmentMainHomeBinding? = null
    private val binding get() = _binding!!

    private val publicPostViewModel by viewModels<PublicPostViewModel> ()

    @Inject
    lateinit var tokenManager: TokenManager

    val bestForYouRecSpace = VerticalSpace()
    val nearRecSpace = HorizontalSpace()

    private lateinit var bestForYouAdapter: PublicFeedBestForYouAdapter
    private lateinit var nearByPostAdapter: PublicFeedNearByPostAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainHomeBinding.inflate(inflater, container, false)
        bestForYouAdapter = PublicFeedBestForYouAdapter(this::onDetailsCliked)
        nearByPostAdapter = PublicFeedNearByPostAdapter(this::onDetailsCliked)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        publicPostViewModel.applicationContext = requireContext()
        publicPostViewModel.publicPost()
        // Setup sticky header

        if (tokenManager.getToken() != null ){



            if (tokenManager.getUserLocation() != null && tokenManager.getUserLocation() != ""){
                publicPostViewModel.nearestPost(tokenManager.getUserLocation()!!)
                binding.nearfromyouRecyler.isVisible = true
                binding.textView7.isVisible = true

            }else{
                binding.textView7.isVisible = false
                binding.nearfromyouRecyler.isVisible = false
            }

        }else{
            binding.textView7.isVisible = false
            binding.nearfromyouRecyler.isVisible = false
        }


      nearByPostRecylerviewIntialize()
        bestForYouRecylerviewIntialize()

        bindOvservers()
        bindOvserversNears()

    }

    // Near by post recylerview intialize
    fun nearByPostRecylerviewIntialize(){
        binding.nearfromyouRecyler.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.HORIZONTAL,false)
        binding.nearfromyouRecyler.addItemDecoration(nearRecSpace)
        binding.nearfromyouRecyler.adapter = nearByPostAdapter
    }

    // Best for you recylerview intialize
    fun bestForYouRecylerviewIntialize(){
        binding.bestForYouRecylerView.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL,false)
        binding.bestForYouRecylerView.addItemDecoration(bestForYouRecSpace)
        binding.bestForYouRecylerView.adapter = bestForYouAdapter

    }

    private fun bindOvserversNears() {

        publicPostViewModel.nearestPostResponeLiveData.observe(viewLifecycleOwner, Observer {result ->
            //   binding?.logprogressBar?.isVisible = false
            when(result){

                is NetworkResult.Success -> {
                    if(result.data!!.status == 200){

                        binding.skeletonLayout2.isVisible = false
                        binding.nearfromyouRecyler.isVisible = true

                        val adsItems = result.data.data.filter { it.isAds && it.isApproved && !it.isSold }
                        val otherItems = result.data.data.filter { !it.isAds && it.isApproved && !it.isSold }

                        val nearestitem = adsItems + otherItems

                        nearByPostAdapter.submitList(nearestitem)


                    }else if(result.data.status == 500){

                        result.message?.let { it1 ->
                            AlertDaialog.showCustomAlertDialogBox(
                                requireContext(),
                                it1
                            )
                        }
                    }
                }
                is NetworkResult.Error -> {
                    // showCustomAlertDialogBox(requireContext() , result.message ?: "Something went wrong")
                }
                is NetworkResult.Loading -> {

                    binding.skeletonLayout2.isVisible = true
                    binding.nearfromyouRecyler.isVisible = false
                }
            }
        })
    }

    private fun bindOvservers() {
        publicPostViewModel.publicPostResponseLiveData.observe(viewLifecycleOwner, Observer {result ->
            //   binding?.logprogressBar?.isVisible = false
            when(result){

                is NetworkResult.Success -> {
                    if(result.data!!.status == 200){


                        binding.skeletonLayout.isVisible = false
                        binding.bestForYouRecylerView.isVisible = true


                        val adsItems = result.data.data.filter { it.isAds && it.isApproved && !it.isSold }
                        val otherItems = result.data.data.filter { !it.isAds && it.isApproved && !it.isSold }

                        val bestForYouResult = adsItems + otherItems


                        bestForYouAdapter.submitList(bestForYouResult)



                    }else if(result.data.status == 500){

                        result.message?.let { it1 ->
                            AlertDaialog.showCustomAlertDialogBox(
                                requireContext(),
                                it1
                            )
                        }
                    }
                }
                is NetworkResult.Error -> {
                    AlertDaialog.showCustomAlertDialogBox(
                        requireContext(),
                        result.message ?: "Something went wrong"
                    )

                    binding.skeletonLayout.isVisible = true
                    binding.bestForYouRecylerView.isVisible = false
                }
                is NetworkResult.Loading -> {
                    //   binding?.logprogressBar?.isVisible = true

                    binding.skeletonLayout.isVisible = true
                    binding.bestForYouRecylerView.isVisible = false
                }



            }



        })


    }

    private fun onDetailsCliked(publicPostData: PublicPostData) {

        println("PublicPostDataCheck: $publicPostData")

        if (tokenManager.getToken() == null){
            val bundle = Bundle()

            bundle.putString("DestinationPage", "Home")

            replaceFragment(NotLogIn(),bundle)
        }else {
            val bundle = Bundle()
            bundle.putString("details", Gson().toJson(publicPostData))
            bundle.putString("DestinationPage", "Home")

            replaceFragment(PostDetails(), bundle)
        }
    }


    private fun replaceFragment(fragment: Fragment,bundle: Bundle) {


        val parentFragmentManager = requireParentFragment().parentFragmentManager
        fragment.arguments = bundle
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentConthainerView4, fragment)
            .addToBackStack(null)
            .commit()


    }




}