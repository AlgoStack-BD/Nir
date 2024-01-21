package com.algostack.nir.view.Auth

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import com.algostack.nir.R
import com.algostack.nir.databinding.FragmentPassVarificationBinding
import com.algostack.nir.services.api.VerificationAPI
import com.algostack.nir.services.model.UpStatusData
import com.algostack.nir.services.model.UpdateStatusRequest
import com.algostack.nir.services.model.VerifyRequest
import com.algostack.nir.utils.AlertDaialog.showCustomAlertDialogBox
import com.algostack.nir.utils.NetworkResult
import com.algostack.nir.utils.TokenManager
import com.algostack.nir.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class PassVarification : Fragment() {

    private var _binding : FragmentPassVarificationBinding ?= null
    private val binding get() = _binding

    private val authViewModel by viewModels<AuthViewModel> ()

    @Inject
    lateinit var tokenManager: TokenManager

    @Inject
    lateinit var verificationAPI: VerificationAPI

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPassVarificationBinding.inflate(inflater,container,false)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fieldOne = binding?.fieldOne
        val fieldTow = binding?.fieldTwo
        val fieldThree = binding?.fieldThree
        val fieldFour = binding?.fieldFour

        val editTextList =  listOf(fieldOne,fieldTow,fieldThree,fieldFour)

        for (i in 0 until editTextList.size - 1){
            val currentEditText = editTextList[i]
            val nextEditText  = editTextList[i + 1]

            currentEditText?.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun afterTextChanged(editable: Editable?) {
                  if(editable.toString().length == 1){
                      nextEditText?.requestFocus()
                  }
                }

            })
        }

        editTextList.last()?.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//
//                if (p0 != null && p0.isNotEmpty() && !p0.toString().matches(Regex("\\d"))) {
//                    // If not a digit, clear the input
//                    currentEditText.text.clear()
//                }

            }

            override fun afterTextChanged(editable: Editable?) {

                if(editable.toString().length == 1){
                    editTextList.last()?.clearFocus()
                }

            }


        })


        binding?.otpContinue?.setOnClickListener {
            val one = binding?.fieldOne?.text.toString()
            val two = binding?.fieldTwo?.text.toString()
            val three = binding?.fieldThree?.text.toString()
            val four = binding?.fieldFour?.text.toString()

            val result = one+two+three+four


            val email = arguments?.getString("email")


            authViewModel.applicationContext = requireContext()

            authViewModel.verifyOTP(VerifyRequest(email,result))



//            CoroutineScope(Dispatchers.IO).launch {
//
//                        val response = verificationAPI.vedifyOTP(VerifyOtpRequest(email,result))
//                        if (response.isSuccessful) {
//                            Log.d(TAG2, "Response Code: ${response.code()}")
//                            Log.d(TAG2, response.body().toString())
//                            findNavController().navigate(R.id.action_veryfyFragment_to_reg_success)
//                        } else {
//                            Log.d(TAG2, "Error: ${response.code()}")
//                        }
//                    }

            bindObservers()

        }


    }

    private fun bindObservers(){
        authViewModel.VerifyOtpMessageLiveData.observe(viewLifecycleOwner, Observer {
            binding?.progressBar?.isVisible = false
            when(it){
                is NetworkResult.Success -> {

                    if (it.data != null && it.data.status == 200 && it.data.message != null) {



                        val id = it.data.message
                        authViewModel.updateVerifyStatus(id, UpdateStatusRequest(UpStatusData(true)))
                        val  sign = arguments?.getBoolean("Sign")

                        println("signCheck: $sign")

                        if(sign == true){
                            findNavController().navigate(R.id.action_passVarification_to_registrationSuccess)
                        }else if(sign == false){
                            val email = arguments?.getString("email")
                            val bundle = Bundle()
                            bundle.putString("email", email)

                            findNavController().navigate(R.id.action_passVarification_to_newPassword,bundle)
                        }




                    }

                }
                is NetworkResult.Error -> {
                    it.message?.let { it1 -> showCustomAlertDialogBox(requireContext(),it1) }

                }
                is NetworkResult.Loading -> {
                    binding?.progressBar?.isVisible = true
                }

                else -> {}
            }

        })
    }




}