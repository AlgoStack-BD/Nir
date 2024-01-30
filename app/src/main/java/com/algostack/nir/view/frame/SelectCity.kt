package com.algostack.nir.view.frame

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AppCompatEditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.algostack.nir.R
import com.algostack.nir.databinding.FragmentSelectCityBinding
import com.algostack.nir.services.model.Cityes
import com.algostack.nir.view.adapter.CityAdapter
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Locale


class SelectCity : Fragment() {


    private var _binding : FragmentSelectCityBinding ?= null
    private val binding get() = _binding!!

    private lateinit var cityArrayList : ArrayList<Cityes>
    private lateinit var cityAdapter : CityAdapter
    private lateinit var searchView: SearchView




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSelectCityBinding.inflate(inflater,container,false)


        setupBackPress()


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        cityArrayList = ArrayList()
        cityAdapter = CityAdapter(cityArrayList){ selectedCity ->
            // Handle the click event for the selected city
            Toast.makeText(requireContext(), "Selected City: ${selectedCity.cityName}", Toast.LENGTH_SHORT).show()
        }
        cityItemList()


        binding.recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(requireContext())
       binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.adapter = cityAdapter


        val searchEditText = binding.searchEditText

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
                    binding.recyclerView.adapter = cityAdapter

                }else{
                    cityAdapter.setFilter(charSequence.toString())
                }
                cityAdapter.setFilter(charSequence.toString())
            }

            override fun afterTextChanged(editable: Editable?) {

            }
        })











    }







    private fun setupBackPress() {
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Handle the back button event
                if(isEnabled){



                    isEnabled = false
                    requireActivity().onBackPressed()
                }


            }
        }
        )
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



}