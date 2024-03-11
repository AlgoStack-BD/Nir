package com.algostack.nir.view.main_frame

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.algostack.nir.R
import com.algostack.nir.databinding.FragmentContactUsBinding

class ContactUs : Fragment() {

 private var _binding : FragmentContactUsBinding?= null
    private val binding get() =  _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentContactUsBinding.inflate(inflater,container,false)
        setupBackPress()
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.textView20.setOnClickListener {

            // call intent
            val number = binding.textView20.text.toString()
            val intent = android.content.Intent(android.content.Intent.ACTION_DIAL, android.net.Uri.parse("tel:$number"))
            startActivity(intent)

        }

        binding.compoanyemail.setOnClickListener {
            val email = binding.compoanyemail.text.toString()
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            startActivity(intent)
        }


        binding.textView26.setOnClickListener {

            // call intent
            val number = binding.textView26.text.toString()
            val intent = Intent(Intent.ACTION_DIAL, android.net.Uri.parse("tel:$number"))
            startActivity(intent)

        }


        binding.developerMail.setOnClickListener {
            val email = binding.developerMail.text.toString()
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            startActivity(intent)
        }



    }



    private fun setupBackPress() {
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Handle the back button event
                if (isEnabled) {

                    val fragmentManager = parentFragmentManager
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.fragmentConthainerView4,Home())
                    fragmentTransaction.remove(this@ContactUs)

                }


            }
        })


    }

}