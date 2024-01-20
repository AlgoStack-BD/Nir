package com.algostack.nir.view.frame

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.algostack.nir.R
import com.algostack.nir.databinding.FragmentPostDetailsBinding
import com.algostack.nir.services.model.ImageItem
import com.algostack.nir.services.model.PublicPostData
import com.algostack.nir.view.adapter.ImageDetailsSmallViewAdapter
import com.google.gson.Gson
import java.util.UUID

class PostDetails : Fragment() {

    private var _binding: FragmentPostDetailsBinding? = null
    private val binding get() = _binding!!
    val newImageArray = arrayListOf<ImageItem>()


    private var detailsData: PublicPostData? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPostDetailsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setInialData()


        val imageAdapter = ImageDetailsSmallViewAdapter()
         binding.imageRV.adapter = imageAdapter
        imageAdapter.submitList(newImageArray)


    }

    private fun setInialData() {
        val jsonDetails = arguments?.getString("details")
        if (jsonDetails != null) {
            detailsData = Gson().fromJson(jsonDetails, PublicPostData::class.java)

            detailsData.let { it ->
                binding.txtOwnerName.text = it?.userName

                // println("ChekLink: "+it!!.img)
                val imagArray: List<String> = it!!.img.split(",")    // split the string
                imagArray.forEach {
                    println("ChekLink: " + it)
                }


                imagArray.forEach {
                    newImageArray.add(ImageItem(UUID.randomUUID().toString(), it))
                }


            }
        }


    }
}