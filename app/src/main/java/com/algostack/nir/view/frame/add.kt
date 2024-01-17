package com.algostack.nir.view.frame

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.algostack.nir.databinding.FragmentAddBinding
import com.algostack.nir.services.model.CreatData
import com.algostack.nir.services.model.CreatePost
import com.algostack.nir.utils.FileCompressor
import com.algostack.nir.utils.NetworkResult
import com.algostack.nir.utils.TokenManager
import com.algostack.nir.viewmodel.PublicPostViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject


@AndroidEntryPoint
class add : Fragment() {


    companion object {

        private const val PERMISSION_REQUEST_CODE = 100
        private const val REQUEST_OPEN_GALLERY = 2
        private const val STORAGE_PERMISSION_CODE = 23

    }


    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var tokenManager: TokenManager


    private val publicPostViewModel by viewModels<PublicPostViewModel> ()
    private lateinit var fileImage : File
    private lateinit var  fileCompressor: FileCompressor
    private lateinit var dialog: AlertDialog
    private var listImage: MutableList<File> = ArrayList()
    private val imageUris = ArrayList<Uri>()
    //private var selectedSelectImage: Int = 0
    private val listSelectImage = arrayOf("Take Photo", "Choose from Gallery")

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








    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // photo picker
        binding.addphoto.setOnClickListener {

          //  selectedSelectImage = 1
           // startGalleryIntent()

            if(checkPermission()){
                openGalleryForImage()

               // startGalleryIntent()
            }else{
                requestPermission()
                println("checkPermission: ${checkPermission()}")
                Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()

            }




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
    publicPostViewModel.addMultipleImages(imageUris)



    bindObserver()


}




    }


    // bind observer service
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


    // photo picker
    private fun openGalleryForImage() {

        if(Build.VERSION.SDK_INT >= 19){
            var intent = Intent()
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_OPEN_GALLERY)
        }
        else{
            // For latast versions API LEVEL 19+
            var intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_OPEN_GALLERY)
        }

    }


//    private fun takePhoto() {
//        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        fileImage = createFile()
//        val uri = if(Build.VERSION.SDK_INT >= 24){
//            FileProvider.getUriForFile(requireContext(), "com.gunawan.multipleimages.fileprovider",
//                fileImage)
//        } else {
//            Uri.fromFile(fileImage)
//        }
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
//        startActivityForResult(intent, REQUEST_TAKE_PHOTO)
//    }

    @SuppressLint("SimpleDateFormat")
    @Throws(Exception::class)
    private fun createFile(): File {
       val timeStemp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir = requireContext().getExternalFilesDir(MediaStore.ACTION_IMAGE_CAPTURE)
        return File.createTempFile("IMG_${timeStemp}", ".jpg", storageDir)
    }


    // Permission for camera and gallery
    private fun checkPermission(): Boolean {
//        val readPermission = ContextCompat.checkSelfPermission(
//            requireContext(),
//            Manifest.permission.READ_EXTERNAL_STORAGE
//        ) == PackageManager.PERMISSION_GRANTED
//
//
//        val writePermission = ContextCompat.checkSelfPermission(
//            requireContext(),
//            Manifest.permission.WRITE_EXTERNAL_STORAGE
//        ) == PackageManager.PERMISSION_GRANTED
//
//
//        return readPermission && writePermission


        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            //Android is 11 (R) or above
            Environment.isExternalStorageManager()
        } else {
            //Below android 11
            val write =
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
            val read =
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
            read == PackageManager.PERMISSION_GRANTED && write == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestPermission() {
//        ActivityCompat.requestPermissions(
//            requireActivity(),
//            arrayOf(
//                Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                Manifest.permission.READ_EXTERNAL_STORAGE
//            ),
//            PERMISSION_REQUEST_CODE
//        )

        //Android is 11 (R) or above
        //Android is 11 (R) or above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val manageStorageIntent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
            manageStorageIntent.data = Uri.parse("package:" + requireContext().packageName)
            startActivity(manageStorageIntent)
        } else {
            //Below android 11
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                STORAGE_PERMISSION_CODE
            )
        }
    }



    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                //startGalleryIntent()
                openGalleryForImage()
            } else {
                Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun startGalleryIntent() {
        val intent = Intent().apply {
            type = "image/*"
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            action = Intent.ACTION_GET_CONTENT
        }
        startActivityForResult(
            Intent.createChooser(intent, "Select Picture"),
            REQUEST_OPEN_GALLERY
        )
    }
private fun bitmapToFile(bitmap: Bitmap): File {
    return try {
        fileImage = createFile()
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos)
        val bitmapdata = bos.toByteArray()

        val fos = FileOutputStream(fileImage)
        fos.write(bitmapdata)
        fos.flush()
        fos.close()
        fileImage
    } catch (e: Exception) {
        e.printStackTrace()
        fileImage
    }
}




    @RequiresApi(Build.VERSION_CODES.P)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_OPEN_GALLERY) {
            if (data?.clipData != null) {

                val count = data.clipData!!.itemCount


                for (i in 0 until count) {
                    val imageUri: Uri = data.clipData!!.getItemAt(i).uri
                     println("ChekImageuri: $imageUri")
                    imageUris.add(imageUri)

                    when (i) {
                        0 -> binding.imagepicker1.setImageURI(imageUri)
                        1 -> binding.imagepicker2.setImageURI(imageUri)
                        2 -> binding.imagepicker3.setImageURI(imageUri)
                        3 -> binding.imagepicker4.setImageURI(imageUri)
                    }
                }
            } else if (data?.data != null) {
                val imageUri: Uri = data.data!!
                imageUris.add(imageUri)
                // Do something with the image (save it to some directory or whatever you need to do with it here)
                // Set image to first ImageView
                binding.imagepicker1.setImageURI(imageUri)
            }
        }
    }
}