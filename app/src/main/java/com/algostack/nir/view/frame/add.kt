package com.algostack.nir.view.frame

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import android.Manifest
import android.app.Activity
import android.content.Intent

import com.algostack.nir.R
import com.algostack.nir.databinding.FragmentAddBinding
import com.algostack.nir.utils.ManagePermission
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class add : Fragment() {


    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    private val PermissionRequestCode = 200

    private lateinit var manaagePermission: ManagePermission
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Initialize a list of requested permissions to request runtime
        val list = listOf<String>(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        )

        // Initialize a new instance of ManagePermissions class
        manaagePermission = ManagePermission(requireActivity(), list, PermissionRequestCode)


        // photo picker
        binding.addphoto.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                manaagePermission.checkPermission()
            }

            openGalleryForImage()
        }















        // spiner for rent type
        binding.renttypespinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    binding.renttypespinner.setSelection(0)
                }

                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    binding.renttypespinner.setSelection(position)
//                    Toast.makeText(
//                        requireContext(),
//                        "Selected: ${adapterView?.getItemAtPosition(position).toString()}",
//                        Toast.LENGTH_SHORT
//                    ).show()
                }

            }

        // spiner for bead Rooms
        val customBeadroomList = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10","11","12","13","14","15",
            "16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50")
        val adapter = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            customBeadroomList
        )

        binding.beadroomspinner.adapter = adapter

        binding.beadroomspinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    binding.beadroomspinner.setSelection(0)
                }

                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    binding.beadroomspinner.setSelection(position)
//                    Toast.makeText(
//                        requireContext(),
//                        "Selected: ${adapterView?.getItemAtPosition(position).toString()}",
//                        Toast.LENGTH_SHORT
//                    ).show()
                }

            }

        // spiner for drawing Rooms
        val customDrawingroomList = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10","11","12","13","14","15",
            "16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50")
        val adapter2 = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            customDrawingroomList
        )

        binding.drawingroomspinner.adapter = adapter2

        binding.drawingroomspinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    binding.drawingroomspinner.setSelection(0)
                }

                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    binding.drawingroomspinner.setSelection(position)
//                    Toast.makeText(
//                        requireContext(),
//                        "Selected: ${adapterView?.getItemAtPosition(position).toString()}",
//                        Toast.LENGTH_SHORT
//                    ).show()
                }

            }


        // spinner for dining Rooms
        val customDiningroomList = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10","11","12","13","14","15",
            "16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50")
        val adapter3 = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            customDiningroomList
        )

        binding.diningroomspinner.adapter = adapter3

        binding.diningroomspinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    binding.diningroomspinner.setSelection(0)
                }

                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    binding.diningroomspinner.setSelection(position)
//                    Toast.makeText(
//                        requireContext(),
//                        "Selected: ${adapterView?.getItemAtPosition(position).toString()}",
//                        Toast.LENGTH_SHORT
//                    ).show()
                }

            }


        // spinner for bath Rooms
        val customBathroomList = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10","11","12","13","14","15",
            "16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50")
        val adapter4 = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            customBathroomList
        )
        binding.bathroomspinner.adapter = adapter4
        binding.bathroomspinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    binding.bathroomspinner.setSelection(0)
                }

                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    binding.bathroomspinner.setSelection(position)
//                    Toast.makeText(
//                        requireContext(),
//                        "Selected: ${adapterView?.getItemAtPosition(position).toString()}",
//                        Toast.LENGTH_SHORT
//                    ).show()
                }

            }

        // spinner for kitchen Rooms
        val customKitchenList = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10","11","12","13","14","15",
            "16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50")
        val adapter5 = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            customKitchenList
        )
        binding.kitchenspinner.adapter = adapter5
        binding.kitchenspinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    binding.kitchenspinner.setSelection(0)
                }

                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    binding.kitchenspinner.setSelection(position)
//                    Toast.makeText(
//                        requireContext(),
//                        "Selected: ${adapterView?.getItemAtPosition(position).toString()}",
//                        Toast.LENGTH_SHORT
//                    ).show()
                }

            }

        // spinner for balcony Rooms
        val customBalconyList = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10","11","12","13","14","15",
            "16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50")
        val adapter6 = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            customBalconyList
        )
        binding.balconyspinner.adapter = adapter6
        binding.balconyspinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    binding.balconyspinner.setSelection(0)
                }

                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    binding.balconyspinner.setSelection(position)
//                    Toast.makeText(
//                        requireContext(),
//                        "Selected: ${adapterView?.getItemAtPosition(position).toString()}",
//                        Toast.LENGTH_SHORT
//                    ).show()
                }

            }


//        val checkedRadioButtonId = binding.radioGroup.checkedRadioButtonId // Returns View.NO_ID if nothing is checked.
//       binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
//            // Responds to child RadioButton checked/unchecked
//        }
//
//// To check a radio button
//        binding.radioButton.isChecked = true
//
//// To listen for a radio button's checked/unchecked state changes
//        binding.radioButton.setOnCheckedChangeListener { buttonView, isChecked
//            // Responds to radio button being checked/unchecked
//        }
//




    }

    private fun openGalleryForImage() {

        if(Build.VERSION.SDK_INT >= 19){
            var intent = Intent()
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PermissionRequestCode)
        }
        else{
            // For latast versions API LEVEL 19+
            var intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            startActivityForResult(intent, PermissionRequestCode)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK && requestCode == PermissionRequestCode){
            // If multiple images are selected
            if(data?.clipData != null){
                val count = data.clipData!!.itemCount
                for(i in 0 until count){
                    val imageUri: Uri = data.clipData!!.getItemAt(i).uri

                    //do something with the image (save it to some directory or whatever you need to do with it here)
                    // set image to image view in one by one


                    if(i == 1){
                        binding.imagepicker1.setImageURI(imageUri)
                    }else if (i == 2) {
                        binding.imagepicker2.setImageURI(imageUri)
                    }else if (i == 3) {
                        binding.imagepicker3.setImageURI(imageUri)
                    }
                    else if (i == 4){
                        binding.imagepicker4.setImageURI(imageUri)
                    }

                }
            }
        }else if(data?.data != null){
            // if single image is selected
            val imageUri: Uri = data.data!!

            //do something with the image (save it to some directory or whatever you need to do with it here)
            // set image to image view
            binding.imagepicker1.setImageURI(imageUri)


        }


    }
}