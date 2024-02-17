package com.algostack.nir.view.frame

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment

import com.algostack.nir.R
import com.algostack.nir.databinding.FragmentProfileMenuBinding
import com.algostack.nir.services.db.NirLocalDB
import com.algostack.nir.utils.TokenManager
import com.algostack.nir.view.main.Frame
import com.algostack.nir.view.main.MainActivity
import com.bumptech.glide.Glide
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileMenu : Fragment() {

    private var _binding : FragmentProfileMenuBinding  ?= null
    private  val binding get() = _binding!!

   @Inject
   lateinit var tokenManager: TokenManager

    @Inject
   lateinit var nirLocalDB: NirLocalDB
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentProfileMenuBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


//        lifecycleScope.launch (Dispatchers.IO){
//           username = nirLocalDB.getLoginInfo().getName().toString()
//            profileImage = nirLocalDB.getLoginInfo().getImage().toString()
//
//        }


        Glide
            .with(requireContext())
            .load("https://nir-house-renting-service-65vv8.ondigitalocean.app/uploads/${tokenManager.getUserImage()}")
            .centerCrop()
            .placeholder(R.drawable.profile)
            .into(binding.profileImage)
        binding.profileName.text = tokenManager.getUserName()

        binding.profileCard.setOnClickListener {
            replaceFragment(ProfileDetails(),ProfileDetails::class.java.simpleName)
        }

        binding.signoutCard.setOnClickListener {

            val intent = Intent(activity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            tokenManager.clearToken()
            activity?.startActivity(intent)

        }

    }

    private fun replaceFragment(fragment: Fragment, flag: String){
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentConthainerView4,fragment,flag).addToBackStack(flag).commit()
    }

}