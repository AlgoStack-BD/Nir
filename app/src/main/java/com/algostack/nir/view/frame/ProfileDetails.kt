package com.algostack.nir.view.frame

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.compose.runtime.snapshots.Snapshot.Companion.observe
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.algostack.nir.R
import com.algostack.nir.databinding.FragmentProfileDetailsBinding
import com.algostack.nir.utils.AlertDaialog
import com.algostack.nir.utils.NetworkResult
import com.algostack.nir.utils.TokenManager
import com.algostack.nir.view.adapter.UserOwnPostAdapte
import com.algostack.nir.view.adapter.VerticalSpace
import com.algostack.nir.viewmodel.ProfileViewModel
import com.bumptech.glide.Glide
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileDetails : Fragment() {

    private var _binding: FragmentProfileDetailsBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var tokenManager: TokenManager

    private lateinit var userOwnPostAdapte: UserOwnPostAdapte

    private val profileViewModel by viewModels<ProfileViewModel>()
    val bestForYouRecSpace = VerticalSpace()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentProfileDetailsBinding.inflate(inflater, container, false)

        userOwnPostAdapte = UserOwnPostAdapte()
        setupBackPress()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.mylistRV.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.mylistRV.addItemDecoration(bestForYouRecSpace)
        binding.mylistRV.adapter = userOwnPostAdapte

        val selected =
            ContextCompat.getDrawable(requireContext(), R.drawable.buttonclickedbackground);
        val default =
            ContextCompat.getDrawable(requireContext(), R.drawable.horizontal_button_circle);
        val selectedColour = ContextCompat.getColor(requireContext(), R.color.white)
        val defaultColour = ContextCompat.getColor(requireContext(), R.color.colorSecendaryBlack)

        binding.profileMyList.setOnClickListener {

            profileViewModel.singleUserPost()
            ViewCompat.setBackground(binding.profileMyList, selected)
            ViewCompat.setBackground(binding.profilefvrt, default)

            binding.mylist.setTextColor(selectedColour)
            binding.fvrtsection.setTextColor(defaultColour)

        }

        binding.profilefvrt.setOnClickListener {

            ViewCompat.setBackground(binding.profileMyList, default)
            ViewCompat.setBackground(binding.profilefvrt, selected)

            binding.mylist.setTextColor(defaultColour)
            binding.fvrtsection.setTextColor(selectedColour)
        }



        Glide
            .with(requireContext())
            .load(tokenManager.getUserImage())
            .centerCrop()
            .placeholder(R.drawable.profile)
            .into(binding.profileimg)

        binding.userName.text = tokenManager.getUserName()
        binding.userEmail.text = tokenManager.getUserEmail()

        bindOverserver()

    }

    private fun bindOverserver() {
        profileViewModel._userPostLiveData.observe(viewLifecycleOwner, Observer { result ->
            //   binding?.logprogressBar?.isVisible = false
            when (result) {

                is NetworkResult.Success -> {
                    if (result.data!!.status == 200) {

                        userOwnPostAdapte.submitList(result.data.data)


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

    private fun setupBackPress() {
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Handle the back button event
                if (isEnabled) {
                    val navBar = activity?.findViewById<BottomAppBar>(R.id.bottomAppBar)
                    val flotBar = activity?.findViewById<FloatingActionButton>(R.id.fab)
                    navBar?.isVisible = true
                    flotBar?.isVisible = true

                    isEnabled = false
                    requireActivity().onBackPressed()
                }


            }
        })


    }
}