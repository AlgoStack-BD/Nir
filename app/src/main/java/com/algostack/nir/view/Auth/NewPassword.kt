package com.algostack.nir.view.Auth


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.algostack.nir.R
import com.algostack.nir.databinding.FragmentNewPasswordBinding
import com.algostack.nir.services.model.ForgetPassData
import com.algostack.nir.services.model.ForgetPasswordRequest
import com.algostack.nir.utils.AlertDaialog.showCustomAlertDialogBox
import com.algostack.nir.utils.NetworkResult
import com.algostack.nir.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewPassword : Fragment() {

  private var _binding : FragmentNewPasswordBinding ?= null
    private val  binding get() = _binding

    private val authViewModel by viewModels<AuthViewModel> ()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentNewPasswordBinding.inflate(inflater,container,false)

        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.newpassContinue?.setOnClickListener {
            val newPASS =  binding?.forgetNewPass?.text.toString().trim()
            val conemail = binding?.forgetPass?.text.toString().trim()

            if(newPASS.isEmpty() || conemail.isEmpty()){

                showCustomAlertDialogBox(requireContext(), "Filed can't empty")

            }else if(newPASS != conemail){

                showCustomAlertDialogBox(requireContext(), "Password didn't matched!")
                println("checkNew: $newPASS")
                println("checkCon: $conemail")
            }else{
                authViewModel.applicationContext = requireContext()
                val email = arguments?.getString("email")
                authViewModel.ResetPassword(ForgetPasswordRequest(ForgetPassData(email!!,newPASS)))

            }


        }

        bindObservers()

    }



    private fun bindObservers(){
        authViewModel.userResponeLiveData.observe(viewLifecycleOwner, Observer {

            binding?.progressBar?.isVisible = false
            when(it){
                is NetworkResult.Success -> {


                    if ( it.data!!.status == 200 ) {
                        // Login success, navigate to home fragment
                        findNavController().navigate(R.id.action_newPassword_to_signin)


                    } else if (it.data!!.status == 404){

                        showCustomAlertDialogBox(requireContext(), it.message ?:  "User not found")
                    }else if(it.data!!.status == 500){

                        showCustomAlertDialogBox( requireContext(),it.message ?: "Internal Server Error")
                    }

                }
                is NetworkResult.Error -> {

                    showCustomAlertDialogBox( requireContext(),it.message ?: "Something went wrong")
                }
                is NetworkResult.Loading -> {

                    binding?.progressBar?.isVisible = true
                }
            }

        })
    }



    override fun onResume() {
        super.onResume()

        requireActivity().onBackPressedDispatcher.addCallback(/* owner = */ viewLifecycleOwner){
            findNavController().navigate(R.id.action_newPassword_to_signin)

        }
    }

}