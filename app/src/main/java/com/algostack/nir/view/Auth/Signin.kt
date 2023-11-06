package com.algostack.nir.view.Auth

import android.app.AlertDialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.BoringLayout
import android.text.InputType
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.algostack.nir.R
import com.algostack.nir.databinding.FragmentSigninBinding
import com.algostack.nir.services.model.UserLoginRequest
import com.algostack.nir.services.model.UserSigninRequest
import com.algostack.nir.utils.NetworkResult
import com.algostack.nir.utils.TokenManager
import com.algostack.nir.view.frame.Frame
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

//            val intent = Intent (getActivity(), frame::class.java)
//            getActivity()?.startActivity(intent)

            val validationResul = validateUserInput()

            if(validationResul.first){
                authViewModel.loginUser(getUserRequest())
            }else{
               //  binding.txtError.text = validationResul.second

                showCustomAlertDialogBox( validationResul.second)
            }



        }



        bindObservers()

//       binding?.Continue?.setOnClickListener {
//           val intent = Intent(getActivity(), Frame::class.java)
//           getActivity()?.startActivity(intent)
//       }

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
//                    println("ChekSuccess call")
//                    val intent = Intent (getActivity(), frame::class.java)
//                    getActivity()?.startActivity(intent)

                    if (it.data != null && it.data.status == 200 && it.data.jwt != null) {
                        // Login success, navigate to home fragment
                        tokenManager.saveToken(it.data!!.jwt)
                        val intent = Intent(getActivity(), Frame::class.java)
                        getActivity()?.startActivity(intent)



                    } else if (it.data!!.status == 404){
//                        binding.txtError.text = it.message ?: "User does not exist with this credentials"
                        val errorMsg = it.message ?: "User does not exist with this credentials"

                        showCustomAlertDialogBox(errorMsg)
                    }else if(it.data!!.status == 401){
                      //  binding.txtError.text = it.message ?: "User is not verified"
                        showCustomAlertDialogBox( it.message ?: "User is not verified")
                    }else if(it.data!!.status == 500){
                        // binding.txtError.text = it.message ?: "Internal Server Error"
                        it.message?.let { it1 -> showCustomAlertDialogBox(it1) }
                    }

                }
                is NetworkResult.Error -> {
                   // binding.txtError.text = it.message ?: "Something went wrong"

                    showCustomAlertDialogBox( it.message ?: "Something went wrong")
                }
                is NetworkResult.Loading -> {
                    binding?.logprogressBar?.isVisible = true
                }
            }

        })
    }


    fun showCustomAlertDialogBox(msg : String){
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.custom_alert_box, null,false)
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(view)

        val alert = builder.create()
        alert.setCancelable(true)

        val cancelBtn = view.findViewById<TextView>(R.id.cencelbtn)
        val okBtn = view.findViewById<TextView>(R.id.okBtn)
        val textView = view.findViewById<TextView>(R.id.alertText)

        textView.text = msg

        cancelBtn.setOnClickListener {
            alert.dismiss()
        }

        okBtn.setOnClickListener {
            alert.dismiss()
        }

        alert.window?.setBackgroundDrawable(ColorDrawable(0))
        alert.show()



    }
    override fun onResume() {
        super.onResume()

        requireActivity().onBackPressedDispatcher.addCallback(/* owner = */ viewLifecycleOwner){
            findNavController().navigate(R.id.action_signin_to_access_nav)

        }
    }



}