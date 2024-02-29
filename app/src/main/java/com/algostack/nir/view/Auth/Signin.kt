package com.algostack.nir.view.Auth

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
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
import com.algostack.nir.databinding.FragmentSigninBinding
import com.algostack.nir.services.model.UserSigninRequest
import com.algostack.nir.utils.AlertDaialog.showCustomAlertDialogBox
import com.algostack.nir.utils.NetworkResult
import com.algostack.nir.utils.TokenManager
import com.algostack.nir.view.main.Frame
import com.algostack.nir.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Signin : Fragment() {

    private var _binding : FragmentSigninBinding ?= null
    private val binding get() = _binding
    private val authViewModel by viewModels<AuthViewModel> (  )
     var passwordVisibility: Boolean = false

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       _binding = FragmentSigninBinding.inflate(inflater,container,false)


        // password visible and hide
        val logpass = binding?.logpass

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

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding?.Continue?.setOnClickListener{



            val validationResul = validateUserInput()

            if(validationResul.first){
                authViewModel.applicationContext = requireContext()
                authViewModel.loginUser(getUserRequest())
            }else{


                showCustomAlertDialogBox(requireContext() , validationResul.second)
            }



        }



        bindObservers()

        binding?.forgetPassword?.setOnClickListener {
            findNavController().navigate(R.id.action_signin_to_forgetPassword)
        }




    }



    private fun getUserRequest() : UserSigninRequest {
        val email = binding?.logEmail?.text.toString()
        val pass = binding?.logpass?.text.toString()

        return UserSigninRequest(email,pass)
    }

    private fun validateUserInput() : Pair<Boolean, String>{
        val  UserRequest = getUserRequest()
        if (TextUtils.isEmpty(UserRequest.password)) { // Check if password field is empty
            return Pair(false, "Password field cannot be empty")
        }

        return authViewModel.validateCredintials("",UserRequest.email,UserRequest.password,"",true)
    }

    private fun bindObservers(){
        authViewModel.userResponeLiveData.observe(viewLifecycleOwner, Observer {
            binding?.logprogressBar?.isVisible = false
            when(it){
                is NetworkResult.Success -> {


                    if (it.data != null && it.data.status == 200) {

                      if(it.data.data.isBanned){
                          showCustomAlertDialogBox(requireContext(), "Your account has been banned from this app")

                      }else {

                          tokenManager.saveToken(
                              it.data!!.jwt,
                              it.data!!.data.name,
                              it.data!!.data.image,
                              it.data!!.data.email,
                              it.data!!.data._id,
                              it.data!!.data.phone,
                              it.data!!.data.location
                          )

                          println("User Token : ${it.data.data}")


                          val intent = Intent(activity, Frame::class.java)
                          intent.flags =
                              Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                          activity?.startActivity(intent)
                          // current activity distroy
                          activity?.finish()
                      }

                    } else if (it.data!!.status == 404){

                        val errorMsg = it.message ?: "User does not exist with this credentials"

                        showCustomAlertDialogBox(requireContext(),errorMsg)
                    }else if(it.data!!.status == 401){

                        showCustomAlertDialogBox( requireContext(),it.message ?: "User is not verified")
                    }else if(it.data!!.status == 500){

                        it.message?.let { it1 -> showCustomAlertDialogBox(requireContext(),it1) }
                    }

                }
                is NetworkResult.Error -> {


                    showCustomAlertDialogBox(requireContext() , it.message ?: "Something went wrong")
                }
                is NetworkResult.Loading -> {
                    binding?.logprogressBar?.isVisible = true
                }
            }

        })
    }



    override fun onResume() {
        super.onResume()

        requireActivity().onBackPressedDispatcher.addCallback(/* owner = */ viewLifecycleOwner){
            findNavController().navigate(R.id.action_signin_to_access_nav)

        }
    }



}