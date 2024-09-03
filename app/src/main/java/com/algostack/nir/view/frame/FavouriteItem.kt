package com.algostack.nir.view.frame

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.algostack.nir.R

import com.algostack.nir.databinding.FragmentFavouriteItemBinding
import com.algostack.nir.services.model.PublicPostData
import com.algostack.nir.services.model.RemoveFavouriteItem
import com.algostack.nir.utils.AlertDaialog
import com.algostack.nir.utils.NetworkResult
import com.algostack.nir.utils.TokenManager
import com.algostack.nir.view.adapter.UserOwnFavouriteListAdapter
import com.algostack.nir.view.adapter.VerticalSpace
import com.algostack.nir.viewmodel.FavouriteViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FavouriteItem : Fragment() {


    private var _binding : FragmentFavouriteItemBinding ?= null
    private  val binding get() = _binding!!

    private val favouriteViewModel by viewModels<FavouriteViewModel>()
    val bestForYouRecSpace = VerticalSpace()
    private lateinit var userFavouritePostAdapte: UserOwnFavouriteListAdapter
    @Inject
    lateinit var tokenManager: TokenManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

            _binding = FragmentFavouriteItemBinding.inflate(inflater,container,false)
           userFavouritePostAdapte = UserOwnFavouriteListAdapter(this::onDetailsCliked)
            return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        println("testuserID: ${tokenManager.getUserId()}")
        val userId = tokenManager.getUserId()!!
        favouriteViewModel.applicationContext = requireContext()
        favouriteViewModel.getUserFavourite(userId)

        binding.favlisRV.layoutManager =
            GridLayoutManager(requireContext(),2)
        binding.favlisRV.addItemDecoration(bestForYouRecSpace)
        binding.favlisRV.adapter = userFavouritePostAdapte

        bindObserver()
    }


    private fun bindObserver() {
        favouriteViewModel.userFavouritePost.observe(viewLifecycleOwner, Observer { result ->

            when (result) {

                is NetworkResult.Success -> {
                    if (result.data!!.status == 200) {

                        if(result.data.data.isEmpty()) {
                            binding.lotti.isVisible = true
                        }else{
                            binding.lotti.isVisible = false
                            userFavouritePostAdapte.submitList(result.data.data)
                        }



                    } else if (result.data.status == 500) {

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


                }

                is NetworkResult.Loading -> {

                }
            }
        })
    }

    private fun onDetailsCliked(_id: String, from: String,publicPostData: PublicPostData) {


      if (from == "removefavourite") {
            val removefavorite = RemoveFavouriteItem(
                _id
            )

            favouriteViewModel.applicationContext = requireContext()
            favouriteViewModel.updateFavorite(tokenManager.getUserId()!!, removefavorite)

            favouriteViewModel.favouritePost.observe(viewLifecycleOwner, Observer { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        if (result.data!!.status == 200) {
                            val userId = tokenManager.getUserId()!!
                            favouriteViewModel.applicationContext = requireContext()
                            favouriteViewModel.getUserFavourite(userId)
                        } else if (result.data.status == 500) {
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
                    }
                    is NetworkResult.Loading -> {
                        //   binding?.logprogressBar?.isVisible = true
                    }
                }
            })
        }
       else if (from == "details") {
            val bundle = Bundle()
            bundle.putString("details", Gson().toJson(publicPostData))
            bundle.putString("DestinationPage", "ProfileDetails")

            replaceFragment(PostDetails(),bundle)
        }


        println("testfrom: $from , $_id , $publicPostData")


    }
    private fun replaceFragment(fragment: Fragment,bundle: Bundle){
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragment.arguments = bundle
        fragmentTransaction.replace(R.id.fragmentConthainerView4,fragment)

        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()

    }


}