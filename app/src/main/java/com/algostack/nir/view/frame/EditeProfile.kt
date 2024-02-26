package com.algostack.nir.view.frame

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
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
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.algostack.nir.R
import com.algostack.nir.databinding.FragmentEditeProfileBinding
import com.algostack.nir.services.model.BillsX
import com.algostack.nir.services.model.Cityes
import com.algostack.nir.services.model.CreatData
import com.algostack.nir.services.model.CreatePost
import com.algostack.nir.services.model.UpdateUserData
import com.algostack.nir.services.model.UserRequest
import com.algostack.nir.services.model.UserUpdateRequest
import com.algostack.nir.services.model.userData
import com.algostack.nir.utils.NetworkResult
import com.algostack.nir.utils.TokenManager
import com.algostack.nir.view.adapter.CityAdapter
import com.algostack.nir.viewmodel.AuthViewModel
import com.algostack.nir.viewmodel.FilterViewModel
import com.algostack.nir.viewmodel.ImageUploadViewModel
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject


@AndroidEntryPoint
class EditeProfile : Fragment() {


    companion object {

        private const val PERMISSION_REQUEST_CODE = 100
        private const val REQUEST_OPEN_GALLERY = 2
        private const val STORAGE_PERMISSION_CODE = 23


    }

    private var _binding: FragmentEditeProfileBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var tokenManager: TokenManager

    private lateinit var fileImage : File
    private var listImage: MutableList<File> = ArrayList()
    private val imageUris = ArrayList<Uri>()
    var selectedImage = ""

    private val updateViewmodel by viewModels<AuthViewModel> ()
    private val imageUploadViewModel by viewModels<ImageUploadViewModel> ()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentEditeProfileBinding.inflate(inflater, container, false)
           setupBackPress()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        Glide
            .with(requireContext())
            .load("https://nir-house-renting-service-65vv8.ondigitalocean.app/uploads/${tokenManager.getUserImage()}")
            .centerCrop()
            .placeholder(R.drawable.profileedit)
            .into(binding.profileimg)

        println("tokenManager.getUserImage(): ${tokenManager.getUserImage()}")

        binding.editUserName.setText(tokenManager.getUserName())
        binding.editUserEmail.setText(tokenManager.getUserEmail())
        binding.editUserPhone.setText(tokenManager.getUserNumber())



        binding.editeProfimgilebtn.setOnClickListener {

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


        binding.updateProfile.setOnClickListener {

            Log.d("CheckFile", "")


            if (imageUris.size >= 0) {

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


    fun createFinalCallPost() {



        val userName = binding.editUserName.text.toString()
        val userPhone = binding.editUserPhone.text.toString()

        if (userName.isEmpty()  || userPhone.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show()
        } else {
            println("tokenManager.getUserId()!!: ${tokenManager.getUserId()}")
            updateViewmodel.updateUserInfo(tokenManager.getUserId()!!, UserUpdateRequest(UpdateUserData(userName, userPhone,selectedImage)))
        }

        bindObserver()
    }




    private fun bindObserverforImageUpload() {
        imageUploadViewModel.uploadImageResponseLiveData.observe(viewLifecycleOwner) {

            binding.progressBar.isVisible = false

            when (it) {
                is NetworkResult.Success -> {
                    if (it.data != null ) {
                        Toast.makeText(requireContext(), "Edit Succesfull", Toast.LENGTH_SHORT).show()
                        listImage.clear()
                        imageUris.clear()

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


    private fun bindObserver() {
        updateViewmodel.userUpdateResponseLiveData.observe(viewLifecycleOwner) {

            binding.progressBar.isVisible = false

            when (it) {
                is NetworkResult.Success -> {
                    if (it.data != null && it.data.status == 200) {
                        Toast.makeText(requireContext(), "Update Successful", Toast.LENGTH_SHORT).show()

                        tokenManager.updateToken(binding.editUserName.text.toString(),binding.editUserPhone.text.toString(),selectedImage)

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

    private fun openGalleryForImage() {


        // For latast versions API LEVEL 19+
//            var intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
//            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
//            intent.addCategory(Intent.ACTION_PICK)
//            intent.type = "image/*"
//            startActivityForResult(intent, REQUEST_OPEN_GALLERY)


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



    @SuppressLint("SimpleDateFormat")
    @Throws(Exception::class)
    private fun createFile(): File {
        val timeStemp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir = requireContext().getExternalFilesDir(MediaStore.ACTION_IMAGE_CAPTURE)
        return File.createTempFile("IMG_${timeStemp}", ".jpg", storageDir)
    }


    // Permission for camera and gallery
    private fun checkPermission(): Boolean {

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



    @RequiresApi(Build.VERSION_CODES.P)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_OPEN_GALLERY) {
            if (data?.data != null) {
                val imageUri: Uri = data.data!!
                imageUris.add(imageUri)
                // Do something with the image (save it to some directory or whatever you need to do with it here)
                // Set image to first ImageView
                binding.profileimg.setImageURI(imageUri)
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


    private fun setupBackPress() {
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Handle the back button event
                if(isEnabled){
                    val fragmentManager = parentFragmentManager
                    val fragmentTransaction = fragmentManager.beginTransaction()

                    fragmentTransaction.replace(R.id.fragmentConthainerView4,ProfileDetails())
                    fragmentTransaction.remove(this@EditeProfile)

                    fragmentTransaction.commit()
                }


            }
        }
        )
    }





}