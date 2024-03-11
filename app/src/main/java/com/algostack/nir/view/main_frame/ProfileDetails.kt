package com.algostack.nir.view.main_frame

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.algostack.nir.R
import com.algostack.nir.databinding.FragmentProfileDetailsBinding
import com.algostack.nir.utils.TokenManager
import com.algostack.nir.view.adapter.ProfileViewPagerAdapter
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileDetails : Fragment() {

    private var _binding: FragmentProfileDetailsBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var tokenManager: TokenManager


    private  lateinit var dialog: BottomSheetDialog


    private val tabTitle = arrayListOf("Listings", "Favourite", "Sold")






    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentProfileDetailsBinding.inflate(inflater, container, false)







        setupBackPress()
        setTabLayout()

        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



       binding.editeProfilebtn.setOnClickListener {
           val fragmentManager = parentFragmentManager
           val fragmentTransaction = fragmentManager.beginTransaction()
           fragmentTransaction.replace(R.id.fragmentConthainerView4,EditeProfile(),EditeProfile::class.java.simpleName)

           fragmentTransaction.addToBackStack(EditeProfile::class.java.simpleName)
           fragmentTransaction.commit()
         }



        Glide
            .with(requireContext())
            .load("https://nir-house-renting-service-65vv8.ondigitalocean.app/uploads/${tokenManager.getUserImage()}")
            .centerCrop()
            .placeholder(R.drawable.profile)
            .into(binding.profileimg)

        binding.userName.text = tokenManager.getUserName()
        binding.userEmail.text = tokenManager.getUserEmail()






    }

    private fun setTabLayout() {
        binding.viewPager.adapter = ProfileViewPagerAdapter(this)

        TabLayoutMediator(binding.tab, binding.viewPager) { tab, position ->
            tab.text = tabTitle[position]
        }.attach()

        // set this textview on the customView of the TabLayout
        for (i in 0..2) {
            val textView = LayoutInflater.from(requireContext()).inflate(R.layout.tab_title, null) as TextView
            binding.tab.getTabAt(i)?.customView = textView
        }

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