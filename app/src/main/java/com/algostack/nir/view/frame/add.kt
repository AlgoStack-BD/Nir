package com.algostack.nir.view.frame

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.algostack.nir.R
import com.algostack.nir.databinding.FragmentAddBinding
import com.algostack.nir.services.model.Cityes
import com.algostack.nir.services.model.CreatData
import com.algostack.nir.services.model.CreatePost
import com.algostack.nir.utils.FileCompressor
import com.algostack.nir.utils.NetworkResult
import com.algostack.nir.utils.TokenManager
import com.algostack.nir.view.adapter.CityAdapter
import com.algostack.nir.viewmodel.ImageUploadViewModel
import com.algostack.nir.viewmodel.PublicPostViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
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
    private val imageUploadViewModel by viewModels<ImageUploadViewModel> ()
    private lateinit var fileImage : File
    private var listImage: MutableList<File> = ArrayList()
    private val imageUris = ArrayList<Uri>()
    private  lateinit var dialog: BottomSheetDialog
    private lateinit var cityArrayList : ArrayList<Cityes>
    private lateinit var cityAdapter : CityAdapter
    private lateinit var recyclerView: RecyclerView


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
    var phoneNumber = ""









    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        cityArrayList = ArrayList()

        cityItemList()

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



        binding.beadroomspinner.setOnClickListener {
            showBottomSheetDialog("Beadroom")
        }





        binding.drawingroomspinner.setOnClickListener {
            showBottomSheetDialog("Drawingroom")
        }





        binding.diningroomspinner.setOnClickListener {
            showBottomSheetDialog("Diningroom")
        }





        binding.bathroomspinner.setOnClickListener {
            showBottomSheetDialog("Bathroom")
        }

        binding.kitchenspinner.setOnClickListener {
            showBottomSheetDialog("Kitchen")
        }


        binding.balconyspinner.setOnClickListener {
            showBottomSheetDialog("Balcony")
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



    if (imageUris.size > 0) {
        for (i in 0 until imageUris.size) {
            // val file = File(imageUris[i].path!!)
            // Log.d("CheckFile", "onViewCreated: ${file.name}")
            val file = File(getRealPathFromURI(imageUris[i], requireContext())!!)
            Log.d("CheckFile", "onViewCreated: ${file}")
            listImage.add(file)
        }
        imageUploadViewModel.addMultipleImages(listImage)


    }


    bindObserverforImageUpload()


}




    }


    private fun createFinalCallPost() {
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
                tokenManager.getUserId()!!,
                userName,
                phoneNumber,
                false

                ))


        publicPostViewModel.createPost(creatPost)


        // clear all fields
        binding.fieldpickaddress.text.clear()
        binding.additionalMessage.text.clear()
        binding.rentpriceinputfield.text.clear()
        binding.imagepicker1.setImageResource(0)
        binding.imagepicker2.setImageResource(0)
        binding.imagepicker3.setImageResource(0)
        binding.imagepicker4.setImageResource(0)
        binding.checkboxelectricity.isChecked = false
        binding.checkboxwater.isChecked = false
        binding.checkbocgass.isChecked = false
        binding.negotiablecheckbox.isChecked = false
        binding.renttypespinner.setSelection(0)
        binding.beadroomspinner.setText("0")
        binding.drawingroomspinner.setText("0")
        binding.diningroomspinner.setText("0")
        binding.bathroomspinner.setText("0")
        binding.kitchenspinner.setText("0")
        binding.balconyspinner.setText("0")
        binding.phonoNumberFiled.text.clear()



        bindObserver()
    }

    private fun bindObserverforImageUpload() {
        imageUploadViewModel.uploadImageResponseLiveData.observe(viewLifecycleOwner) {

            binding.progressBar.isVisible = false

            when (it) {
                is NetworkResult.Success -> {
                    if (it.data != null ) {
                        Toast.makeText(requireContext(), "Post Created", Toast.LENGTH_SHORT).show()

                        for (i in it.data.fileNames.indices) {
                            if (i == 0) {
                                selectedImage = it.data.fileNames[i]
                            } else {
                                selectedImage = selectedImage + "," + it.data.fileNames[i]
                            }
                        }


                        createFinalCallPost()


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



    fun getRealPathFromURI(uri: Uri, context: Context): String? {
        val returnCursor = context.contentResolver.query(uri, null, null, null, null)
        val nameIndex =  returnCursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        val sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE)
        returnCursor.moveToFirst()
        val name = returnCursor.getString(nameIndex)
        val size = returnCursor.getLong(sizeIndex).toString()
        val file = File(context.filesDir, name)
        try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(file)
            var read = 0
            val maxBufferSize = 1 * 1024 * 1024
            val bytesAvailable: Int = inputStream?.available() ?: 0
            //int bufferSize = 1024;
            val bufferSize = Math.min(bytesAvailable, maxBufferSize)
            val buffers = ByteArray(bufferSize)
            while (inputStream?.read(buffers).also {
                    if (it != null) {
                        read = it
                    }
                } != -1) {
                outputStream.write(buffers, 0, read)
            }
            Log.e("File Size", "Size " + file.length())
            inputStream?.close()
            outputStream.close()
            Log.e("File Path", "Path " + file.path)

        } catch (e: java.lang.Exception) {
            Log.e("Exception", e.message!!)
        }
        return file.path
    }



    private fun showBottomSheetDialog(check: String) {
        dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        val view = layoutInflater.inflate(R.layout.select_number_item, null)

        dialog.setContentView(view)

        val searchEditText = view.findViewById<androidx.appcompat.widget.AppCompatEditText>(R.id.search_edit_text_bttom)

        val save = view.findViewById<TextView>(R.id.saveText)
        save.setOnClickListener {
            binding.beadroomspinner.setText(searchEditText.text.toString())
            dialog.dismiss()
        }



        recyclerView = view.findViewById(R.id.bottomrecyclerView)
        cityAdapter = CityAdapter(cityArrayList){
            //binding.locationText.text = it.cityName
            if(check == "Beadroom"){
                binding.beadroomspinner.setText(it.cityName)
                selectedBeadroom = it.cityName.toIntOrNull() ?: 0
            }else if(check == "Drawingroom"){
                binding.drawingroomspinner.setText(it.cityName)
                selectedDrawingroom = it.cityName.toIntOrNull() ?: 0
            }else if(check == "Diningroom"){
                binding.diningroomspinner.setText(it.cityName)
                selectedDiningroom = it.cityName.toIntOrNull() ?: 0
            }else if(check == "Bathroom"){
                binding.bathroomspinner.setText(it.cityName)
                selectedBathroom = it.cityName.toIntOrNull() ?: 0
            }else if(check == "Kitchen"){
                binding.kitchenspinner.setText(it.cityName)
                selectedKitchen = it.cityName.toIntOrNull() ?: 0
            }else if(check == "Balcony"){
                binding.balconyspinner.setText(it.cityName)
                selectedBalcony = it.cityName.toIntOrNull() ?: 0
            }
            dialog.dismiss()
        }


        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = cityAdapter



        dialog.show()
    }

    private fun cityItemList(){
        cityArrayList.add(Cityes(1,"1"))
        cityArrayList.add(Cityes(2,"2"))
        cityArrayList.add(Cityes(3,"3"))
        cityArrayList.add(Cityes(4,"4"))
        cityArrayList.add(Cityes(5,"5"))
        cityArrayList.add(Cityes(6,"6"))
        cityArrayList.add(Cityes(7,"7"))
        cityArrayList.add(Cityes(8,"8"))
        cityArrayList.add(Cityes(9,"9"))
        cityArrayList.add(Cityes(10,"10"))
        cityArrayList.add(Cityes(11,"11"))
        cityArrayList.add(Cityes(12,"12"))
        cityArrayList.add(Cityes(13,"13"))
        cityArrayList.add(Cityes(14,"14"))
        cityArrayList.add(Cityes(15,"15"))
        cityArrayList.add(Cityes(16,"16"))
        cityArrayList.add(Cityes(17,"17"))



    }


}