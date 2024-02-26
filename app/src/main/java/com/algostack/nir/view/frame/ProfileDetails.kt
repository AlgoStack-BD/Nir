package com.algostack.nir.view.frame

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.algostack.nir.R
import com.algostack.nir.databinding.FragmentProfileDetailsBinding
import com.algostack.nir.services.model.PaymentRequest
import com.algostack.nir.services.model.PaymentRequestData
import com.algostack.nir.services.model.RemoveFavouriteItem
import com.algostack.nir.utils.AlertDaialog
import com.algostack.nir.utils.NetworkResult
import com.algostack.nir.utils.TokenManager
import com.algostack.nir.view.adapter.CityAdapter
import com.algostack.nir.view.adapter.UserOwnFavouriteListAdapter
import com.algostack.nir.view.adapter.UserOwnPostAdapte
import com.algostack.nir.view.adapter.VerticalSpace
import com.algostack.nir.viewmodel.FavouriteViewModel
import com.algostack.nir.viewmodel.ProfileViewModel
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileDetails : Fragment() {

    private var _binding: FragmentProfileDetailsBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var tokenManager: TokenManager


    private  lateinit var dialog: BottomSheetDialog
    private lateinit var userOwnPostAdapte: UserOwnPostAdapte
    private lateinit var userFavouritePostAdapte: UserOwnFavouriteListAdapter

    private val profileViewModel by viewModels<ProfileViewModel>()

    private val favouriteViewModel by viewModels<FavouriteViewModel>()
    val bestForYouRecSpace = VerticalSpace()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentProfileDetailsBinding.inflate(inflater, container, false)

        userOwnPostAdapte = UserOwnPostAdapte(this::onDetailsCliked)
        userFavouritePostAdapte = UserOwnFavouriteListAdapter(this::onDetailsCliked)
        setupBackPress()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        println("testuserID: ${tokenManager.getUserId()}")
        val userId = tokenManager.getUserId()!!
        profileViewModel.applicationContext = requireContext()
        profileViewModel.singleUserPost(userId)

        favouriteViewModel.applicationContext = requireContext()
        favouriteViewModel.getUserFavourite(userId)



        val selected =
            ContextCompat.getDrawable(requireContext(), R.drawable.buttonclickedbackground);
        val default =
            ContextCompat.getDrawable(requireContext(), R.drawable.horizontal_button_circle);
        val selectedColour = ContextCompat.getColor(requireContext(), R.color.white)
        val defaultColour = ContextCompat.getColor(requireContext(), R.color.colorSecendaryBlack)



       binding.editeProfilebtn.setOnClickListener {
           val fragmentManager = parentFragmentManager
           val fragmentTransaction = fragmentManager.beginTransaction()
           fragmentTransaction.replace(R.id.fragmentConthainerView4,EditeProfile(),EditeProfile::class.java.simpleName)

           fragmentTransaction.addToBackStack(EditeProfile::class.java.simpleName)
           fragmentTransaction.commit()
         }


        binding.profileMyList.setOnClickListener {


            ViewCompat.setBackground(binding.profileMyList, selected)
            ViewCompat.setBackground(binding.profilefvrt, default)
            binding.mylistRV.isVisible = true
            binding.favlisRV.isVisible = false

            binding.mylist.setTextColor(selectedColour)
            binding.fvrtsection.setTextColor(defaultColour)

            // check if data is aviailable or not
            if (userOwnPostAdapte.itemCount == 0){
                binding.lotti.isVisible = true
            }else{
                binding.lotti.isVisible = false

            }

        }

        binding.profilefvrt.setOnClickListener {

            ViewCompat.setBackground(binding.profileMyList, default)
            ViewCompat.setBackground(binding.profilefvrt, selected)
            binding.mylistRV.isVisible = false
            binding.favlisRV.isVisible = true

            binding.mylist.setTextColor(defaultColour)
            binding.fvrtsection.setTextColor(selectedColour)

            // check if data is aviailable or not
            if (userFavouritePostAdapte.itemCount == 0){
                binding.lotti.isVisible = true
            }else{

                binding.lotti.isVisible = false
            }
        }






        Glide
            .with(requireContext())
            .load("https://nir-house-renting-service-65vv8.ondigitalocean.app/uploads/${tokenManager.getUserImage()}")
            .centerCrop()
            .placeholder(R.drawable.profile)
            .into(binding.profileimg)

        binding.userName.text = tokenManager.getUserName()
        binding.userEmail.text = tokenManager.getUserEmail()

        binding.mylistRV.layoutManager =
            GridLayoutManager(requireContext(),2)
        binding.mylistRV.addItemDecoration(bestForYouRecSpace)
        binding.mylistRV.adapter = userOwnPostAdapte

        binding.favlisRV.layoutManager =
            GridLayoutManager(requireContext(),2)
        binding.favlisRV.addItemDecoration(bestForYouRecSpace)
        binding.favlisRV.adapter = userFavouritePostAdapte

        bindOverserver()
        bindFavObserver()

    }

    private fun bindOverserver() {
        profileViewModel._userPostLiveData.observe(viewLifecycleOwner, Observer { result ->
            //   binding?.logprogressBar?.isVisible = false
            when (result) {

                is NetworkResult.Success -> {
                    if (result.data!!.status == 200) {

                        if(result.data.data.isEmpty()) {
                            binding.lotti.isVisible = true
                        }

                        userOwnPostAdapte.submitList(result.data.data)

                        // check if data is aviailable or not



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

    private fun bindFavObserver() {
        favouriteViewModel.userFavouritePost.observe(viewLifecycleOwner, Observer { result ->
            //   binding?.logprogressBar?.isVisible = false
            when (result) {

                is NetworkResult.Success -> {
                    if (result.data!!.status == 200) {

                        userFavouritePostAdapte.submitList(result.data.data)


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



    private fun onDetailsCliked(_id: String, from: String) {


        if (from == "delete") {
            profileViewModel.deletePost(_id)

            profileViewModel.deletePostResponseLiveData.observe(viewLifecycleOwner, Observer { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        if (result.data!!.status == 200) {
                            val userId = tokenManager.getUserId()!!
                            profileViewModel.applicationContext = requireContext()
                            profileViewModel.singleUserPost(userId)
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
        } else if (from == "removefavourite") {
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
        else if (from == "bostPost") {
            showBottomSheetPaymentsyStemDialog(_id)
        }


    }

    // payment system function
    private fun showBottomSheetPaymentsyStemDialog(postId : String) {
        dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        val view = layoutInflater.inflate(R.layout.paymentsystem, null)

        dialog.setContentView(view)



        // Set up BottomSheet behavior
        val bottomSheetBehavior = BottomSheetBehavior.from(view.parent as View)
//        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED // Close the BottomSheet initially


        // Disable dragging the BottomSheet up
       // bottomSheetBehavior.isDraggable = false



        val payment = view.findViewById<WebView>(R.id.camwebView)



        if (payment != null) {
            payment.apply {

                settings.javaScriptEnabled = true
                settings.setSupportZoom(true)
                settings.builtInZoomControls = true
                settings.displayZoomControls = false
                webViewClient = WebViewClient()
                webChromeClient = WebChromeClient()

                loadUrl("https://buy.stripe.com/test_5kAdSYetSbNhdVufYZ")

            }
                // GET CURRENT URL
                payment.webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        println("testurl: $url")



                        if (url != "https://buy.stripe.com/test_5kAdSYetSbNhdVufYZ") {


                        // if payment success
                        val sessionid= url?.substringAfter("session_id=")
                        println("testsessionid: $sessionid")
                        if (sessionid != null){
                            // dialog cancel
                            dialog.cancel()

                            // payment success
                            paymentSuccess(sessionid,postId)
                            println("paymentfuntest: $sessionid , $postId")
                        }

                    }
                    }


                }

        } else {
            Log.e("WebView", "Payment WebView is null")
        }







        dialog.show()
    }

    private fun paymentSuccess(sessionid: String, postId: String) {

        val userId = tokenManager.getUserId()!!
        profileViewModel.applicationContext = requireContext()
        println("paymentfuntestinter: $sessionid , $userId , $postId")
        profileViewModel.makePayment(PaymentRequest(PaymentRequestData(postId,sessionid,userId) ))

        profileViewModel.paymentResponseLiveData.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is NetworkResult.Success -> {
                    println("checkthesuccessresponse: ${result.data}")
                  if(result.data!!.status == 200){
                      AlertDaialog.showCustomDoneDialogBox(
                          requireContext(),
                          "Payment Success"
                      )
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


    private fun setupBackPress() {
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Handle the back button event
                if (isEnabled) {

                    val fragmentManager = parentFragmentManager
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.fragmentConthainerView4,Home())
                    fragmentTransaction.remove(this@ProfileDetails)

                }


            }
        })


    }
}