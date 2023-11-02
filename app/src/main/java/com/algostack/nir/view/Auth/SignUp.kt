package com.algostack.nir.view.Auth

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import com.algostack.nir.R
import com.algostack.nir.databinding.FragmentSignUpBinding


class SignUp : Fragment() {

    private var _binding : FragmentSignUpBinding ?= null
    private val binding get() =  _binding

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


}