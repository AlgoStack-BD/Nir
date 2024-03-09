package com.algostack.nir.view.frame

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
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
import com.algostack.nir.services.model.RentRequestData
import com.algostack.nir.services.model.RentRequestNotification
import com.algostack.nir.utils.AlertDaialog
import com.algostack.nir.utils.NetworkResult
import com.algostack.nir.utils.TokenManager
import com.algostack.nir.view.adapter.ImageDetailsSmallViewAdapter
import com.algostack.nir.viewmodel.FavouriteViewModel
import com.algostack.nir.viewmodel.NotificationViewModel
import com.algostack.nir.viewmodel.PublicPostViewModel
import com.bumptech.glide.Glide
import com.github.chrisbanes.photoview.PhotoView
import com.google.android.material.datepicker.MaterialDatePicker

import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import de.hdodenhof.circleimageview.CircleImageView
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
    private val notificationViewModel by viewModels<NotificationViewModel> ()
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

        binding.sentRequestforRent.setOnClickListener {


            showBookingDialog(requireContext())

            println("CheckRentRequest: called")



        }

        binding.fvrtDone.setOnClickListener{

            favouriteViewModelrite.applicationContext = requireContext()
            favouriteViewModelrite.updateFavorite(tokenManager.getUserId()!!, RemoveFavouriteItem(detailsData!!._id))
            FbindOvserver()

        }


        val imageAdapter = ImageDetailsSmallViewAdapter(this::onDetailsCliked)
         binding.imageRV.adapter = imageAdapter
        imageAdapter.submitList(newImageArray)


        bindOvserver()

    }

    private fun onDetailsCliked(_id: String, from: String) {

        // show my custom photviewe dialog
        val view = LayoutInflater.from(context).inflate(R.layout.photoviewer, null)
        val builder = AlertDialog.Builder(context)
        builder.setView(view)

        val alert = builder.create()
        alert.setCancelable(true)
        val photoView  = view.findViewById<PhotoView>(R.id.photo_view)

        // load image from server
        Glide
            .with(requireContext())
            .load("https://nir-house-renting-service-65vv8.ondigitalocean.app/uploads/$_id")
            .centerCrop()
            .placeholder(R.drawable.demo_home_photo)
            .into(photoView)


        alert.window?.setBackgroundDrawable(ColorDrawable(0))
        alert.show()

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

                else -> {}
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

                else -> {}
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
                val imagArray: List<String> = it!!.img!!.split(",")    // split the string
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

                if (it.bills.electricBill && it.bills.waterBill && it.bills.gasBill){
                    binding.bills.text = "Electricity   |   Water   |   Gas"
                }
                else if (it.bills.electricBill && it.bills.waterBill){
                    binding.bills.text = "Electricity   |   Water"
                }
                else if (it.bills.electricBill && it.bills.gasBill){
                    binding.bills.text = "Electricity   |   Gas"
                }
                else if (it.bills.waterBill && it.bills.gasBill){
                    binding.bills.text = "Water   |   Gas"
                }
               else if (it.bills.electricBill){
                    binding.bills.text = "Electricity"
                }else if (it.bills.waterBill){
                    binding.bills.text = "Water"
                }else if (it.bills.gasBill){
                    binding.bills.text = "Gas"
                }


                if(it!!.isNegotiable){
                    binding.isNagotiable.isVisible = true
                    binding.rentprice.text = "${it.price.toString()} ৳"
                }else
                {

                    binding.isNagotiable.isVisible = false
                    binding.rentprice.text = "${it.price.toString()} ৳"
                }


            }
        }


    }


    // Booking Dialog
    fun showBookingDialog(context: Context) {
        println("showBookingDialog: done")
        val view = LayoutInflater.from(context).inflate(R.layout.bookingsystem, null)
        val builder = AlertDialog.Builder(context)
        builder.setView(view)

        val alert = builder.create()
        alert.setCancelable(true)

        val confirm = view.findViewById<LinearLayout>(R.id.confirmBooking)
        val cancel = view.findViewById<CircleImageView>(R.id.cancel)
        val selectVisitigDate = view.findViewById<LinearLayout>(R.id.selectVisitingDateLayout)
        val visitingDate = view.findViewById<TextView>(R.id.visiting_date)
        val selectVisitingTime = view.findViewById<LinearLayout>(R.id.selectVisitingTimeLayout)
        val visitingTime = view.findViewById<TextView>(R.id.visiting_time)
        val visitorName = view.findViewById<EditText>(R.id.visitorsname)
        val visitorsNumber = view.findViewById<EditText>(R.id.visitorsnumber)
        val nameSameAsMyProfile = view.findViewById<CheckBox>(R.id.sameasme)
        val numberSameAsMyProfile = view.findViewById<CheckBox>(R.id.sameasme2)

        selectVisitigDate.setOnClickListener {

            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .setTitleText("Select date")
                    .build()

            datePicker.addOnPositiveButtonClickListener {
                val date = datePicker.headerText

                // if select date in today's date to previous date then show error
                if (datePicker.selection!! < MaterialDatePicker.todayInUtcMilliseconds()) {
                    Toast.makeText(context, "Please select a valid date", Toast.LENGTH_SHORT).show()
                    // show error in a alert dialog
                    AlertDialog.Builder(context)
                        .setTitle("Invalid Date")
                        .setMessage("Please select a valid date")
                        .setPositiveButton("OK") { dialog, which ->
                            dialog.dismiss()
                        }
                        .show()

                }else{
                    visitingDate.text = date
                }


            }

            datePicker.show(childFragmentManager, "date_picker_tag")
        }

        selectVisitingTime.setOnClickListener {
            val time = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(10)
                .setTitleText("Select Visiting Time")
                .build()


            time.addOnPositiveButtonClickListener{
                val hour = time.hour
                val minute = time.minute
                // pick am / pm format
                val amPm = if (hour >= 12) "PM" else "AM"

                visitingTime.text = "$hour:$minute $amPm"
            }


            time.show(childFragmentManager, "time_picker_tag")
        }


        nameSameAsMyProfile.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                visitorName.setText(tokenManager.getUserName())
            } else {
                visitorName.setText("")
            }
        }

        numberSameAsMyProfile.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                visitorsNumber.setText(tokenManager.getUserNumber())
            } else {
                visitorsNumber.setText("")
            }
        }

        val clientImage = if (tokenManager.getUserImage() != null) {
           tokenManager.getUserImage()
        } else {
            ""
        }
        confirm.setOnClickListener {
            Toast.makeText(context, "Booking Confirmed", Toast.LENGTH_SHORT).show()

            notificationViewModel.rentRequestNotification(
                RentRequestNotification(
                    RentRequestData(
                        visitorName.text.toString() ?: "",
                        clientImage!!,
                        visitorsNumber.text.toString() ?: "",
                        visitingDate.text.toString(),
                        visitingTime.text.toString(),
                        detailsData!!.userId!!,
                        false,
                        detailsData!!._id,
                        //detailsData!!.title if null then use ""
                        detailsData!!.title ?: "",
                        "pending",
                        tokenManager.getUserId()!!,
                        false,
                        detailsData!!.location ?: ""
                    )
                )
            )
            bindOvserverforRequestRent()
            alert.dismiss()
        }

        cancel.setOnClickListener{
            alert.dismiss()
        }

        alert.window?.setBackgroundDrawable(ColorDrawable(0))
        alert.show()
    }

    private fun bindOvserverforRequestRent() {
        notificationViewModel.rentForRequestResponse.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }

                is NetworkResult.Success -> {
                    println("CheckRentRequest: ${it}")
                    Toast.makeText(requireContext(), "Rent Request Sent", Toast.LENGTH_SHORT).show()

                }

                is NetworkResult.Error -> {
                    println("CheckError: ${it}")

                }

                else -> {}
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
                    }else if (chekDestinationPage == "Notification"){
                        fragmentTransaction.replace(R.id.fragmentConthainerView4,Notification())
                        fragmentTransaction.remove(this@PostDetails)
                    }else if (chekDestinationPage == "ProfileDetails") {
                        fragmentTransaction.replace(R.id.fragmentConthainerView4, ProfileDetails())
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