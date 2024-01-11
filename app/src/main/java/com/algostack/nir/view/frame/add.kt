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
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels

import com.algostack.nir.R
import com.algostack.nir.databinding.FragmentAddBinding
import com.algostack.nir.services.model.CreatData
import com.algostack.nir.services.model.CreatePost
import com.algostack.nir.services.model.PublicPostData
import com.algostack.nir.utils.ManagePermission
import com.algostack.nir.utils.NetworkResult
import com.algostack.nir.utils.TokenManager
import com.algostack.nir.viewmodel.AuthViewModel
import com.algostack.nir.viewmodel.PublicPostViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class add : Fragment() {


    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var tokenManager: TokenManager


    private val publicPostViewModel by viewModels<PublicPostViewModel> ()


    var selectedRentType = ""
    var selectedBeadroom = 0
    var selectedDrawingroom = 0
    var selectedDiningroom = 0
    var selectedBathroom = 0
    var selectedKitchen = 0
    var selectedBalcony = 0
    var selectedRent = 0
    var selectedAddress = ""
    var selectedDescription = ""
    var selectedGass = false
    var selectedWater = false
    var selectedElectricity = false
    var selectedNagotiablity = false
    var rentPrice = 0
    var selectedImage = ""
    var selectAdditonalMessage = ""


    private val PermissionRequestCode = 200
    // again try
  //  private lateinit var managePermissions: ManagePermission




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
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )


        // Initialize a new instance of ManagePermissions class
     //   managePermissions = ManagePermission(requireActivity(),list,PermissionRequestCode)


        // photo picker
        binding.addphoto.setOnClickListener {



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


                    selectedRentType = adapterView?.getItemAtPosition(position).toString()


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

                    selectedBeadroom = adapterView?.getItemAtPosition(position).toString().toIntOrNull() ?: 0

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

                        selectedDrawingroom = adapterView?.getItemAtPosition(position).toString().toIntOrNull() ?: 0
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

                        selectedDiningroom = adapterView?.getItemAtPosition(position).toString().toIntOrNull() ?: 0
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

                    selectedBathroom = adapterView?.getItemAtPosition(position).toString().toIntOrNull() ?: 0
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

                        selectedKitchen = adapterView?.getItemAtPosition(position).toString().toIntOrNull() ?: 0
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

                            selectedBalcony = adapterView?.getItemAtPosition(position).toString().toIntOrNull() ?: 0
                }

            }

        // chekbox for gas
        binding.checkbocgass.setOnCheckedChangeListener { buttonView, isChecked ->
            selectedGass = isChecked
        }

       // chekbox for water
        binding.checkboxwater.setOnCheckedChangeListener { buttonView, isChecked ->
            selectedWater = isChecked
        }

        // chekbox for electricity
        binding.checkboxelectricity.setOnCheckedChangeListener { buttonView, isChecked ->
            selectedElectricity = isChecked
        }

        // chekbox for nagotiable
        binding.negotiablecheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            selectedNagotiablity = isChecked
        }

        // rent price
         selectedRent = binding.rentpriceinputfield.text.toString().toIntOrNull() ?: 0



binding.regContinue.setOnClickListener {
    // address
    selectedAddress = binding.fieldpickaddress.text.toString()
    println("address: $selectedAddress")

    selectAdditonalMessage =  binding.additionalMessage.text.toString()

    publicPostViewModel.applicationContext = requireContext()
    val userName = tokenManager.getUserName().toString()

    val creatPost = CreatePost(
        CreatData(
            selectAdditonalMessage,
        selectedBalcony,
        selectedBathroom,
        selectedBeadroom,
        com.algostack.nir.services.model.BillsX(selectedElectricity,selectedGass,"",selectedWater),
        selectedDiningroom,
        selectedDrawingroom,
        selectedImage,
        false,
        false,
        selectedNagotiablity,
        false,
        false,
        selectedKitchen,
        0,
        selectedAddress,
        selectedRent,
        selectedRentType,
        "",
            userName,

    ))


    publicPostViewModel.createPost(creatPost)


    bindObserver()


}




    }

    private fun bindObserver() {
        publicPostViewModel.createPostResponseLiveData.observe(viewLifecycleOwner) {

            binding.progressBar.isVisible = false

            when (it) {
                is NetworkResult.Success -> {
                    if (it.data != null && it.data.status == 200) {
                        Toast.makeText(requireContext(), "Post Created", Toast.LENGTH_SHORT).show()
                    }
                }

                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }

                is NetworkResult.Error -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun requestRuntimePermission() {
        println("11permission")
        val readPermission = Manifest.permission.READ_EXTERNAL_STORAGE
        val writePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                readPermission
            ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                requireContext(),
                writePermission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(requireContext(), "Permission already granted", Toast.LENGTH_SHORT).show()
            openGalleryForImage()
            println("15permission")
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                readPermission
            ) || ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                writePermission
            )
        ) {
            println("13permission")
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Permission Required")
            builder.setMessage("This app needs storage permission to use this feature.")
            builder.setPositiveButton("OK") { dialog, _ ->
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(readPermission, writePermission),
                    PermissionRequestCode
                )
                dialog.dismiss()
            }
            builder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            builder.show()
        } else {
            println("14permission")
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(readPermission, writePermission),
                PermissionRequestCode
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PermissionRequestCode) {
            if (grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                grantResults[1] == PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(requireContext(), "Permission granted.", Toast.LENGTH_LONG).show()
                openGalleryForImage()
            } else if (!ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    permissions[0]
                ) && !ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    permissions[1]
                )
            ) {
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Permission Required")
                builder.setMessage("This feature is unavailable because you have previously declined this permission request. Please go to Settings and enable the permission to use this feature.")
                builder.setCancelable(false)
                builder.setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                builder.setPositiveButton("Settings") { dialog, _ ->
                    Intent(
                        android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + requireContext().packageName)
                    ).apply {
                        startActivity(this)
                    }
                    dialog.dismiss()
                }
                builder.show()
            } else {
                requestRuntimePermission()
            }
        }
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

        if (resultCode == Activity.RESULT_OK && requestCode == PermissionRequestCode) {
            if (data?.clipData != null) {
                val count = data.clipData!!.itemCount
                for (i in 0 until count) {
                    val imageUri: Uri = data.clipData!!.getItemAt(i).uri
                    // Do something with the image (save it to some directory or whatever you need to do with it here)
                    // Set image to ImageView for each item
                    when (i) {
                        0 -> binding.imagepicker1.setImageURI(imageUri)
                        1 -> binding.imagepicker2.setImageURI(imageUri)
                        2 -> binding.imagepicker3.setImageURI(imageUri)
                        3 -> binding.imagepicker4.setImageURI(imageUri)
                    }
                }
            } else if (data?.data != null) {
                val imageUri: Uri = data.data!!
                // Do something with the image (save it to some directory or whatever you need to do with it here)
                // Set image to first ImageView
                binding.imagepicker1.setImageURI(imageUri)
            }
        }
    }
}