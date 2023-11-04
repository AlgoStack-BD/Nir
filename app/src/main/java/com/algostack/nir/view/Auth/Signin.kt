package com.algostack.nir.view.Auth

import android.content.Intent
import android.os.Bundle
import android.text.BoringLayout
import android.text.InputType
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.algostack.nir.R
import com.algostack.nir.databinding.FragmentSigninBinding
import com.algostack.nir.view.frame.Frame
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Signin : Fragment() {

    private var _binding : FragmentSigninBinding ?= null
    private val binding get() = _binding

     var passwordVisibility: Boolean = false

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



       binding?.Continue?.setOnClickListener {
           val intent = Intent(getActivity(), Frame::class.java)
           getActivity()?.startActivity(intent)
       }

        binding?.forgetPassword?.setOnClickListener {
            findNavController().navigate(R.id.action_signin_to_forgetPassword)
        }




    }






}