package com.algostack.nir.view.Auth

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import com.algostack.nir.R
import com.algostack.nir.databinding.FragmentSignUpBinding
import com.algostack.nir.services.api.VerificationAPI
import com.algostack.nir.services.model.UserRequest
import com.algostack.nir.services.model.VerificationRequest
import com.algostack.nir.services.model.userData
import com.algostack.nir.utils.AlertDaialog.showCustomAlertDialogBox
import com.algostack.nir.utils.NetworkResult
import com.algostack.nir.utils.TokenManager
import com.algostack.nir.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Pattern
import javax.inject.Inject

@AndroidEntryPoint
class SignUp : Fragment() {

    private var _binding : FragmentSignUpBinding ?= null
    private val binding get() =  _binding

    private val authViewModel by viewModels<AuthViewModel> ()

    @Inject
    lateinit var tokenManager: TokenManager

    @Inject
    lateinit var verificationAPI: VerificationAPI

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSignUpBinding.inflate(inflater,container,false)

        // password visible and hide
        val logpass = binding?.regmainpassword

        logpass?.setOnTouchListener { _, event ->
            val DRAWABLE_RIGHT = 2

            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (logpass?.right?.minus(logpass!!.compoundDrawables[DRAWABLE_RIGHT].bounds.width())!!)) {
                    if (logpass.inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                        logpass.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                        logpass.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.visibility_off, 0)
                    } else {
                        logpass.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                        logpass.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.visibilityon, 0)
                    }
                    logpass.setSelection(logpass.text.length)
                    true
                } else {
                    false
                }
            } else {
                false
            }
        }

        val confirmpass = binding?.confirmpas

        confirmpass?.setOnTouchListener { _, event ->
            val DRAWABLE_RIGHT = 2

            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (confirmpass?.right?.minus(confirmpass!!.compoundDrawables[DRAWABLE_RIGHT].bounds.width())!!)) {
                    if (confirmpass.inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                        confirmpass.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                        confirmpass.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.visibility_off, 0)
                    } else {
                        confirmpass.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                        confirmpass.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.visibilityon, 0)
                    }
                    confirmpass.setSelection(confirmpass.text.length)
                    true
                } else {
                    false
                }
            } else {
                false
            }
        }


        return binding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding?.regContinue?.setOnClickListener {
            val validationResult = validateUserInput()

            if(validationResult.first){
                authViewModel.applicationContext = requireContext()
                authViewModel.registerUser(getUserRequest())
            }else{
                showCustomAlertDialogBox(requireContext(), validationResult.second)
            }
        }

        bindObservers()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){
            findNavController().navigate(R.id.action_signUp_to_access_nav)
        }
    }


    private fun getUserRequest(): UserRequest {
        val name = binding?.regname?.text.toString()
        val email = binding?.regemail?.text.toString()
        val password = binding?.regmainpassword?.text.toString()


        return UserRequest(userData(email, "",false,false,"",name,password,"",0,0,false))
    }

    private fun validateUserInput() : Pair<Boolean,String>{
        val confirmPassword = binding?.confirmpas?.text.toString()

        val UserRegRequest = getUserRequest()
        val passwordPattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&#])[A-Za-z\\d@\$!%*?&#]{8,}\$")
        val passwordMatcher = passwordPattern.matcher(UserRegRequest.data.password)


        if (UserRegRequest.data.password.isEmpty() || confirmPassword.isEmpty()) {
            return Pair(false, "Password and confirm password fields cannot be empty")
        }
        if (UserRegRequest.data.password != confirmPassword) {
            return Pair(false, "Passwords do not match")
        }

        if (!passwordMatcher.matches()) {
            return Pair(false, "Password should be 8+ characters with uppercase, \nlowercase, digit, and special character.")
        }



        return authViewModel.validateCredintials(UserRegRequest.data.name,UserRegRequest.data.email,UserRegRequest.data.password,confirmPassword,false)
    }
    private fun bindObservers(){
        authViewModel.signupResponseLiveData.observe(viewLifecycleOwner, Observer {
           binding?.progressBar?.isVisible = false
            when(it){
                is NetworkResult.Success -> {
                    if (it.data != null && it.data.status == 200) {
                        val email = binding?.regemail?.text.toString()
                        tokenManager.saveToken(it.data!!.jwt, it.data!!.responseData.name, it.data!!.responseData.image, it.data!!.responseData.email,it.data!!.responseData._id,it.data!!.responseData.phone,it.data.responseData.location)
                        println("ChekToken: ${tokenManager.getToken()}")
                        authViewModel.VerificationRequest(VerificationRequest(email))
                        val bundle = Bundle()
                        bundle.putString("email", email)
                        bundle.putBoolean("Sign" , true)
                        findNavController().navigate(R.id.action_signUp_to_passVarification, bundle)
                    } else if (it.data!!.status == 400) {
                       val errorMsg = it.message ?: "This email is already registered"

                        showCustomAlertDialogBox(requireContext(),errorMsg)
                    } else if (it.data!!.status == 500) {


                        showCustomAlertDialogBox(requireContext(), it.message ?: "Internal Server Error")
                    }
                }
                is NetworkResult.Error -> {
                   // binding.txtError.text = it.message
                    it.message?.let { it1 -> showCustomAlertDialogBox( requireContext(),it1) }
                }
                is NetworkResult.Loading -> {
                    binding?.progressBar?.isVisible = true
                }

                else -> {}
            }
        })
    }




}