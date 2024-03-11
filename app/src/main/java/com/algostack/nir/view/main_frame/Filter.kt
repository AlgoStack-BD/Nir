package com.algostack.nir.view.main_frame

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.algostack.nir.R
import com.algostack.nir.databinding.FragmentFilterBinding
import com.algostack.nir.services.model.Cityes
import com.algostack.nir.services.model.PublicPostData
import com.algostack.nir.utils.AlertDaialog
import com.algostack.nir.utils.NetworkResult
import com.algostack.nir.utils.TokenManager
import com.algostack.nir.view.adapter.CityAdapter
import com.algostack.nir.view.adapter.PublicFeedBestForYouAdapter
import com.algostack.nir.view.adapter.VerticalSpace
import com.algostack.nir.viewmodel.FilterViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


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


    private  lateinit var dialog: BottomSheetDialog
    private lateinit var cityArrayList : ArrayList<Cityes>
    private lateinit var cityAdapter : CityAdapter
    private lateinit var recyclerView: RecyclerView

    private val filterViewModel by viewModels<FilterViewModel> ()


    val bestForYouRecSpace = VerticalSpace()

    @Inject
    lateinit var tokenManager: TokenManager


    private lateinit var bestForYouAdapter: PublicFeedBestForYouAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFilterBinding.inflate(inflater,container,false)

        bestForYouAdapter = PublicFeedBestForYouAdapter(this::onDetailsCliked)
        setupBackPress()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        cityArrayList = ArrayList()

        cityItemList()

            binding.pricerangeSlider.addOnChangeListener { slider, value, fromUser ->

                binding.endRange.setText( value.toInt().toString())

        }






        binding.LocationPicker.setOnClickListener {
            showBottomSheetDialog()

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



                Log.d(
                    "Filter",
                    "onViewCreated: $minmumPriceRange $maximumPriceRange $LocationArea $numberOfBedRoom $numberOfBathRoom $selectedRentType"
                )

                filterViewModel.filter(
                    minmumPriceRange,
                    maximumPriceRange,
                    minmumPriceRange,
                    numberOfBedRoom,
                    numberOfBathRoom,
                    selectedRentType!!,
                    LocationArea
                )


            }


        binding.filteredResult.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL,false)
        binding.filteredResult.addItemDecoration(bestForYouRecSpace)
        binding.filteredResult.adapter = bestForYouAdapter

        binding.searchAgain.setOnClickListener {
           // findNavController().popBackStack()
            binding.searchResultLayout.isVisible = false
            binding.filterLayout.isVisible = true
        }



        bindOvservers()


    }



    private fun showBottomSheetDialog() {
        dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        val view = layoutInflater.inflate(R.layout.select_city_bottom_view, null)

        dialog.setContentView(view)

        val searchEditText = view.findViewById<androidx.appcompat.widget.AppCompatEditText>(R.id.search_edit_text_bttom)

        recyclerView = view.findViewById(R.id.bottomrecyclerView)
        cityAdapter = CityAdapter(cityArrayList){
            binding.locationText.text = it.cityName
            dialog.dismiss()
        }


        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(requireContext())


        recyclerView.setHasFixedSize(true)

        recyclerView.adapter = cityAdapter


        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed
            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                // Not needed
                if (charSequence.isNullOrEmpty()) {
                    //clear arraylist then reassign
                    cityArrayList.clear()
                    cityItemList()
                    recyclerView.adapter = cityAdapter

                }else{
                    cityAdapter.setFilter(charSequence.toString())
                }
                cityAdapter.setFilter(charSequence.toString())
            }

            override fun afterTextChanged(editable: Editable?) {

            }
        })


        dialog.show()
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
               // println("selectedBedRoom $selectedBedRoom  , selectedBathRoom: $selectedBathRoom" )
            } else if (code == 202) {
                selectedCard2 = card
                selectedBathRoom = numberOfBedrooms
              //  println("selectedBedRoom $selectedBedRoom  , selectedBathRoom: $selectedBathRoom" )
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




    private fun cityItemList(){
        cityArrayList.add(Cityes(1,"Habiganj"))
        cityArrayList.add(Cityes(2,"Moulvibazar"))
        cityArrayList.add(Cityes(3,"Sunamganj"))
        cityArrayList.add(Cityes(4,"Dhaka"))
        cityArrayList.add(Cityes(5,"Chittagong"))
        cityArrayList.add(Cityes(6,"Sylhet"))
        cityArrayList.add(Cityes(7,"Khulna"))
        cityArrayList.add(Cityes(8,"Rajshahi"))
        cityArrayList.add(Cityes(9,"Barisal"))
        cityArrayList.add(Cityes(10,"Rangpur"))
        cityArrayList.add(Cityes(11,"Comilla"))
        cityArrayList.add(Cityes(12,"Narayanganj"))
        cityArrayList.add(Cityes(13,"Gazipur"))
        cityArrayList.add(Cityes(14,"Mymensingh"))
        cityArrayList.add(Cityes(15,"Tangail"))
        cityArrayList.add(Cityes(16,"Bogura"))
        cityArrayList.add(Cityes(17,"Dinajpur"))
        cityArrayList.add(Cityes(18,"Jessore"))
        cityArrayList.add(Cityes(19,"Kustia"))
        cityArrayList.add(Cityes(20,"Naogaon"))
        cityArrayList.add(Cityes(21,"Savar"))
        cityArrayList.add(Cityes(22,"Brahmanbaria"))
        cityArrayList.add(Cityes(23,"Jamalpur"))
        cityArrayList.add(Cityes(24,"Saidpur"))
        cityArrayList.add(Cityes(25,"Sirajganj"))
        cityArrayList.add(Cityes(26,"Pabna"))
        cityArrayList.add(Cityes(27,"Natore"))
        cityArrayList.add(Cityes(28,"Faridpur"))
        cityArrayList.add(Cityes(29,"Pirojpur"))
        cityArrayList.add(Cityes(30,"Bhola"))
        cityArrayList.add(Cityes(31,"Jhalokati"))
        cityArrayList.add(Cityes(32,"Patuakhali"))
        cityArrayList.add(Cityes(33,"Barguna"))
        cityArrayList.add(Cityes(34,"Chandpur"))
        cityArrayList.add(Cityes(35,"Lakshmipur"))
        cityArrayList.add(Cityes(36,"Noakhali"))
        cityArrayList.add(Cityes(37,"Feni"))
        cityArrayList.add(Cityes(38,"Bagerhat"))
        cityArrayList.add(Cityes(39,"Chuadanga"))
        cityArrayList.add(Cityes(40,"Jhenaidah"))
        cityArrayList.add(Cityes(41,"Magura"))
        cityArrayList.add(Cityes(42,"Meherpur"))
        cityArrayList.add(Cityes(43,"Narail"))
        cityArrayList.add(Cityes(44,"Satkhira"))
        cityArrayList.add(Cityes(45,"Khagrachari"))
        cityArrayList.add(Cityes(46,"Rangamati"))
        cityArrayList.add(Cityes(47,"Bandarban"))
        cityArrayList.add(Cityes(48,"Cox's Bazar"))
        cityArrayList.add(Cityes(49,"Thakurgaon"))
        cityArrayList.add(Cityes(50,"Panchagarh"))
        cityArrayList.add(Cityes(51,"Tangail"))
        cityArrayList.add(Cityes(52,"Shariatpur"))
        cityArrayList.add(Cityes(53,"Madaripur"))
        cityArrayList.add(Cityes(54,"Rajbari"))
        cityArrayList.add(Cityes(55,"Gopalganj"))
        cityArrayList.add(Cityes(56,"Kishoreganj"))
        cityArrayList.add(Cityes(57,"Netrokona"))
        cityArrayList.add(Cityes(58,"Sherpur"))
        cityArrayList.add(Cityes(59,"Munshiganj"))
        cityArrayList.add(Cityes(60,"Narsingdi"))
        cityArrayList.add(Cityes(61,"Manikganj"))
        cityArrayList.add(Cityes(62,"Potuakhali"))
        cityArrayList.add(Cityes(63,"Nilphamari"))
        cityArrayList.add(Cityes(64,"Gaibandha"))
        cityArrayList.add(Cityes(65,"Lalmonirhat"))
        cityArrayList.add(Cityes(66,"Kurigram"))
        cityArrayList.add(Cityes(67,"Nawabganj"))

    }


    private fun bindOvservers() {
        filterViewModel.FilterLiveData.observe(viewLifecycleOwner, Observer {result ->
            //   binding?.logprogressBar?.isVisible = false
            when(result){

                is NetworkResult.Success -> {


                    if(result.data!!.status == 200){

                        binding.filterLayout.isVisible = false
                        binding.searchResultLayout.isVisible = true
                        val bestForYouResult = result.data.data

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
                }
                is NetworkResult.Loading -> {
                    //   binding?.logprogressBar?.isVisible = true
                }


                else -> {}
            }



        })


    }


    private fun onDetailsCliked(publicPostData: PublicPostData) {
        if (tokenManager.getToken() == null){
            val bundle = Bundle()

            bundle.putString("DestinationPage", "Filter")

            val parentFragmentManager = parentFragmentManager
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentConthainerView4, NotLogIn())
                .addToBackStack(null)
                .commit()
        }else{
            val bundle = Bundle()
            bundle.putString("details", Gson().toJson(publicPostData))

            replaceFragment(PostDetails(),bundle,PostDetails::class.java.simpleName)
        }

    }

    private fun replaceFragment(fragment: Fragment,bundle: Bundle,flag: String? = null){
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragment.arguments = bundle
        fragmentTransaction.add(R.id.fragmentConthainerView4,fragment,flag)

        fragmentTransaction.addToBackStack(flag)
        fragmentTransaction.commit()

    }


    private fun setupBackPress() {
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Handle the back button event
                if(isEnabled){
                    val fragmentManager = parentFragmentManager
                    val fragmentTransaction = fragmentManager.beginTransaction()

                    fragmentTransaction.replace(R.id.fragmentConthainerView4,Home())
                    fragmentTransaction.remove(this@Filter)

                    fragmentTransaction.commit()
                }


            }
        }
        )
    }

}


