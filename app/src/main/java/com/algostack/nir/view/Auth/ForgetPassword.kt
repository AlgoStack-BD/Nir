package com.algostack.nir.view.Auth

import android.os.Bundle
import android.util.Patterns
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
import com.algostack.nir.databinding.FragmentForgetPasswordBinding
import com.algostack.nir.services.api.VerificationAPI
import com.algostack.nir.services.model.VerificationRequest
import com.algostack.nir.utils.AlertDaialog.showCustomAlertDialogBox
import com.algostack.nir.utils.NetworkResult
import com.algostack.nir.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ForgetPassword : Fragment() {

  private var _binding: FragmentForgetPasswordBinding ?= null
    private val binding get() = _binding

    private val authViewModel by viewModels<AuthViewModel> ()

    @Inject
    lateinit var verificationAPI: VerificationAPI
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForgetPasswordBinding.inflate(inflater,container,false)




        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding?.verifyContinue?.setOnClickListener {

            val email = binding?.forgetemail?.text.toString()

            if (email.isEmpty()) {
                binding?.forgetemail?.error = "Email can't empty"
                binding?.forgetemail?.requestFocus()
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding?.forgetemail?.error = "Email can't empty"
                binding?.forgetemail?.requestFocus()
            } else {
                authViewModel.VerificationRequest(VerificationRequest(email))

                bindObservers()
            }
        }


    }

    private fun bindObservers(){
        authViewModel.VerifyResponseLiveData.observe(viewLifecycleOwner, Observer {
            binding?.progressBar?.isVisible = false
            when(it){
                is NetworkResult.Success -> {

                    if (it.data != null && it.data.status == 200) {


                        val email = binding?.forgetemail?.text.toString()

                        val bundle = Bundle()
                        bundle.putString("email", email)
                        bundle.putBoolean("Sign", false)
                        findNavController().navigate(R.id.action_forgetPassword_to_passVarification, bundle)


                    }

                }
                is NetworkResult.Error -> {
                    it.message?.let { it1 -> showCustomAlertDialogBox(requireContext(),it1) }

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
            findNavController().navigate(R.id.action_forgetPassword_to_signin)

        }
    }


}