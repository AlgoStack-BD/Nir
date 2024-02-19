package com.algostack.nir.view.frame

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.algostack.nir.R
import com.algostack.nir.databinding.FragmentPostDetailsBinding
import com.algostack.nir.services.model.DataXX
import com.algostack.nir.services.model.DataXXX
import com.algostack.nir.services.model.FavouriteRequest
import com.algostack.nir.services.model.FavouriteResponse
import com.algostack.nir.services.model.ImageItem
import com.algostack.nir.services.model.PublicPostData
import com.algostack.nir.services.model.RemoveFavouriteItem
import com.algostack.nir.utils.NetworkResult
import com.algostack.nir.utils.TokenManager
import com.algostack.nir.view.adapter.ImageDetailsSmallViewAdapter
import com.algostack.nir.viewmodel.FavouriteViewModel
import com.algostack.nir.viewmodel.PublicPostViewModel
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

    private var chekDestinationPage = ""
    var callToAction = ""

    private val favouriteViewModelrite by viewModels<FavouriteViewModel> ()
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



        favouriteViewModelrite.applicationContext = requireContext()
        favouriteViewModelrite.getSpecificFavourite(tokenManager.getUserId()!!, detailsData!!._id)

        binding.actionCall.setOnClickListener {
            // open phone call intent for call

            val dialIntent = Intent(Intent.ACTION_DIAL)
            dialIntent.data = android.net.Uri.parse("tel:$callToAction")
            startActivity(dialIntent)


        }

        binding.actionMassage.setOnClickListener {
            // open message intent for  message given number
            val sendIntent = Intent(Intent.ACTION_VIEW)
            sendIntent.data = android.net.Uri.parse("sms:$callToAction")
            startActivity(sendIntent)


        }


        binding.fvrt.setOnClickListener{


            val favorite = FavouriteRequest(
                DataXX(

                    detailsData!!._id,
                    tokenManager.getUserId()!!
                )
            )

            println("checkFavorite: ${favorite}")

            favouriteViewModelrite.applicationContext = requireContext()

            favouriteViewModelrite.createFavorite(favorite)

            FbindOvserver()

        }

        binding.fvrtDone.setOnClickListener{

            favouriteViewModelrite.applicationContext = requireContext()
            favouriteViewModelrite.updateFavorite(tokenManager.getUserId()!!, RemoveFavouriteItem(detailsData!!._id))
            FbindOvserver()

        }


        val imageAdapter = ImageDetailsSmallViewAdapter()
         binding.imageRV.adapter = imageAdapter
        imageAdapter.submitList(newImageArray)


        bindOvserver()
    }

    private fun bindOvserver() {
        favouriteViewModelrite.specificFavouritePost.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Loading -> {

                }

                is NetworkResult.Success -> {

                    if (it.data!!.data) {
                        binding.fvrt.isVisible = false
                        binding.fvrtDone.isVisible = true
                    } else {
                        binding.fvrt.isVisible = true
                        binding.fvrtDone.isVisible = false
                    }
                }

                is NetworkResult.Error -> {
                    println("CheckError: ${it}")
                }
            }
        }
    }

    private fun FbindOvserver() {
        favouriteViewModelrite.favouritePost.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Loading -> {

                }

                is NetworkResult.Success -> {
                    favouriteViewModelrite.applicationContext = requireContext()
                    favouriteViewModelrite.getSpecificFavourite(tokenManager.getUserId()!!, detailsData!!._id)
                }

                is NetworkResult.Error -> {
                    println("CheckError: ${it}")
                }
            }
        }
    }

    private fun setInialData() {

        val destinationPage = arguments?.getString("DestinationPage")
        if (destinationPage != null) {
            chekDestinationPage = destinationPage
        }

        val jsonDetails = arguments?.getString("details")
        if (jsonDetails != null) {
            detailsData = Gson().fromJson(jsonDetails, PublicPostData::class.java)

            Glide
                .with(requireContext())
                .load("https://nir-house-renting-service-65vv8.ondigitalocean.app/uploads/${detailsData?.userImage}")
                .centerCrop()
                .placeholder(R.drawable.profile)
                .into(binding.userProfile)
            binding.ownerName.text = detailsData?.userName

            callToAction = detailsData?.phoneNumber.toString()

            detailsData.let { it ->
                binding.txtOwnerName.text = it?.title

                // println("ChekLink: "+it!!.img)
                val imagArray: List<String> = it!!.img.split(",")    // split the string
                imagArray.forEach {
                    newImageArray.add(ImageItem(UUID.randomUUID().toString(), it))
                }
                binding.txtDescription.text = it.additionalMessage
                binding.location.text = it.location
                binding.numOfBadroom.text = it.bedRoom.toString()
                binding.numOfBbathroom.text = it.bathRoom.toString()
                binding.numOfKitchen.text = it.kitchen.toString()
                binding.numOfDinigroom.text = it.diningRoom.toString()
                binding.numOfDrawing.text = it.drawingRoom.toString()
                binding.numOfBelcony.text = it.balcony.toString()
                binding.rentPriceAmmount.text = "${it.price.toString()} ৳"
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
                    val fragmentManager = parentFragmentManager
                    val fragmentTransaction = fragmentManager.beginTransaction()

                    if (chekDestinationPage == "Home"){
                        fragmentTransaction.replace(R.id.fragmentConthainerView4,Home())
                        fragmentTransaction.remove(this@PostDetails)
                    }
                    else{
                        val bundle = Bundle()
                        bundle.putString("DestinationPage", "PostDetails")
                        val filter = Filter()
                        filter.arguments = bundle
                        fragmentTransaction.replace(R.id.fragmentConthainerView4,filter)
                        fragmentTransaction.remove(this@PostDetails)
                    }


                    fragmentTransaction.commit()
                }


            }
        }
        )
    }




}