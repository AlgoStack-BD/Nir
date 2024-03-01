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
import android.graphics.drawable.ColorDrawable
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
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatEditText
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
import com.algostack.nir.services.model.ImageItem
import com.algostack.nir.services.model.Numbers
import com.algostack.nir.utils.FileCompressor
import com.algostack.nir.utils.LanguageManager
import com.algostack.nir.utils.NetworkResult
import com.algostack.nir.utils.TokenManager
import com.algostack.nir.view.adapter.CityAdapter
import com.algostack.nir.view.adapter.ImageDetailsSmallViewAdapter
import com.algostack.nir.view.adapter.NumbersAdapter
import com.algostack.nir.view.main.MainActivity
import com.algostack.nir.viewmodel.ImageUploadViewModel
import com.algostack.nir.viewmodel.PublicPostViewModel
import com.bumptech.glide.Glide
import com.github.chrisbanes.photoview.PhotoView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.UUID
import javax.inject.Inject
import kotlin.concurrent.timerTask


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
    private lateinit var numberArrayList : ArrayList<Numbers>
    private lateinit var cityAdapter : CityAdapter
    private lateinit var numbersAdapter: NumbersAdapter
    private lateinit var recyclerView: RecyclerView
    val newImageArray = arrayListOf<ImageItem>()




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
    var title = ""










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
        cityItemListorginal()




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

        binding.renttypespinner.setOnClickListener {
            showRentTypeBottomSheetDialog()
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
        binding.fieldpickaddress.setOnClickListener {

            showBottomSheetDialog()
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

     if (tokenManager.getUserImage() == null) {
         val bundle = Bundle()
         bundle.putString("DestinationPage", "Home")
         replaceFragment(NotLogIn(),bundle)
        }else {


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




    }


    private fun createFinalCallPost() {
        // address
        selectedAddress = binding.addressTextFiled.text.toString()
        println("address: $selectedAddress")

        selectAdditonalMessage =  binding.additionalMessage.text.toString()

        selectedRent = binding.rentpriceinputfield.text.toString().toIntOrNull() ?: 0

        val housename = binding.titleTextFiled.text.toString()

        title = "$selectedBeadroom BeadRoom $selectedBathroom BathRoom $selectedRentType Flat for Rent at $housename"

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
                tokenManager.getUserName().toString(),
                tokenManager.getUserNumber().toString(),
                false,
                title,
                tokenManager.getUserImage().toString()

                ))


        publicPostViewModel.createPost(creatPost)


        // clear all fields
        binding.additionalMessage.text.clear()
        binding.rentpriceinputfield.text.clear()
        binding.checkboxelectricity.isChecked = false
        binding.checkboxwater.isChecked = false
        binding.checkbocgass.isChecked = false
        binding.negotiablecheckbox.isChecked = false
        binding.bedroomTextFiled.text = "0"
        binding.drawingroomTextFiled.text = "0"
        binding.diningroomTextFiled.text = "0"
        binding.bathroomTextFiled.text = "0"
        binding.kitchenTextFiled.text = "0"
        binding.balconyTextFiled.text = "0"
        binding.titleTextFiled.text.clear()
        // clear image
        imageUris.clear()
        listImage.clear()
        newImageArray.clear()
        //clear image adapter
        binding.imageRV.isVisible = false



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

    private fun showBottomSheetDialog() {
        dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        val view = layoutInflater.inflate(R.layout.select_city_bottom_view, null)

        dialog.setContentView(view)

        val searchEditText = view.findViewById<androidx.appcompat.widget.AppCompatEditText>(R.id.search_edit_text_bttom)

        recyclerView = view.findViewById(R.id.bottomrecyclerView)
        cityAdapter = CityAdapter(cityArrayList){
            binding.addressTextFiled.text = it.cityName
            //clear arraylist then reassign and clear the adapter



            dialog.dismiss()

        }


        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(requireContext())


        recyclerView.setHasFixedSize(true)

        recyclerView.adapter = cityAdapter


        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed
            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                // Not needed
                if (charSequence.isNullOrEmpty()) {
                    //clear arraylist then reassign
                    cityItemListorginal()
                    recyclerView.adapter = cityAdapter

                }else{
                    cityAdapter.setFilter(charSequence.toString())
                }
                cityAdapter.setFilter(charSequence.toString())
            }

            override fun afterTextChanged(editable: Editable?) {

            }
        })


        dialog.show()
    }

    private fun showRentTypeBottomSheetDialog() {
        dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        val view = layoutInflater.inflate(R.layout.selectrenttype, null)

        dialog.setContentView(view)

        val bachelor = view.findViewById<RadioButton>(R.id.bachelorRadioButton)
        val family = view.findViewById<RadioButton>(R.id.familyRadioButton)
        val sublet = view.findViewById<RadioButton>(R.id.subletRadioButton)
        val famandbachelor = view.findViewById<RadioButton>(R.id.familyRadioButton)

        bachelor.setOnClickListener {
             binding.rentType.text = "Bachelor"
            selectedRentType = "Bachelor"
            dialog.dismiss()
        }

        family.setOnClickListener {
            binding.rentType.text = "Family"
            selectedRentType = "Family"
            dialog.dismiss()
        }

        sublet.setOnClickListener {
            binding.rentType.text = "Sublet"
            selectedRentType = "Sublet"
            dialog.dismiss()
        }

        famandbachelor.setOnClickListener {
            binding.rentType.text = "Family and Bachelor"
            selectedRentType = "Family and Bachelor"
            dialog.dismiss()
        }






        dialog.show()
    }
    // city funtion
    private fun cityItemListorginal(){
        cityArrayList.add(Cityes(1,"Habiganj"))
        cityArrayList.add(Cityes(2,"Moulvibazar"))
        cityArrayList.add(Cityes(3,"Sunamganj"))
        cityArrayList.add(Cityes(4,"Dhaka"))
        cityArrayList.add(Cityes(5,"Chittagong"))
        cityArrayList.add(Cityes(6,"Sylhet"))
        cityArrayList.add(Cityes(7,"Khulna"))
        cityArrayList.add(Cityes(8,"Rajshahi"))
        cityArrayList.add(Cityes(9,"Barisal"))
        cityArrayList.add(Cityes(10,"Rangpur"))
        cityArrayList.add(Cityes(11,"Comilla"))
        cityArrayList.add(Cityes(12,"Narayanganj"))
        cityArrayList.add(Cityes(13,"Gazipur"))
        cityArrayList.add(Cityes(14,"Mymensingh"))
        cityArrayList.add(Cityes(15,"Tangail"))
        cityArrayList.add(Cityes(16,"Bogura"))
        cityArrayList.add(Cityes(17,"Dinajpur"))
        cityArrayList.add(Cityes(18,"Jessore"))
        cityArrayList.add(Cityes(19,"Kustia"))
        cityArrayList.add(Cityes(20,"Naogaon"))
        cityArrayList.add(Cityes(21,"Savar"))
        cityArrayList.add(Cityes(22,"Brahmanbaria"))
        cityArrayList.add(Cityes(23,"Jamalpur"))
        cityArrayList.add(Cityes(24,"Saidpur"))
        cityArrayList.add(Cityes(25,"Sirajganj"))
        cityArrayList.add(Cityes(26,"Pabna"))
        cityArrayList.add(Cityes(27,"Natore"))
        cityArrayList.add(Cityes(28,"Faridpur"))
        cityArrayList.add(Cityes(29,"Pirojpur"))
        cityArrayList.add(Cityes(30,"Bhola"))
        cityArrayList.add(Cityes(31,"Jhalokati"))
        cityArrayList.add(Cityes(32,"Patuakhali"))
        cityArrayList.add(Cityes(33,"Barguna"))
        cityArrayList.add(Cityes(34,"Chandpur"))
        cityArrayList.add(Cityes(35,"Lakshmipur"))
        cityArrayList.add(Cityes(36,"Noakhali"))
        cityArrayList.add(Cityes(37,"Feni"))
        cityArrayList.add(Cityes(38,"Bagerhat"))
        cityArrayList.add(Cityes(39,"Chuadanga"))
        cityArrayList.add(Cityes(40,"Jhenaidah"))
        cityArrayList.add(Cityes(41,"Magura"))
        cityArrayList.add(Cityes(42,"Meherpur"))
        cityArrayList.add(Cityes(43,"Narail"))
        cityArrayList.add(Cityes(44,"Satkhira"))
        cityArrayList.add(Cityes(45,"Khagrachari"))
        cityArrayList.add(Cityes(46,"Rangamati"))
        cityArrayList.add(Cityes(47,"Bandarban"))
        cityArrayList.add(Cityes(48,"Cox's Bazar"))
        cityArrayList.add(Cityes(49,"Thakurgaon"))
        cityArrayList.add(Cityes(50,"Panchagarh"))
        cityArrayList.add(Cityes(51,"Tangail"))
        cityArrayList.add(Cityes(52,"Shariatpur"))
        cityArrayList.add(Cityes(53,"Madaripur"))
        cityArrayList.add(Cityes(54,"Rajbari"))
        cityArrayList.add(Cityes(55,"Gopalganj"))
        cityArrayList.add(Cityes(56,"Kishoreganj"))
        cityArrayList.add(Cityes(57,"Netrokona"))
        cityArrayList.add(Cityes(58,"Sherpur"))
        cityArrayList.add(Cityes(59,"Munshiganj"))
        cityArrayList.add(Cityes(60,"Narsingdi"))
        cityArrayList.add(Cityes(61,"Manikganj"))
        cityArrayList.add(Cityes(62,"Potuakhali"))
        cityArrayList.add(Cityes(63,"Nilphamari"))
        cityArrayList.add(Cityes(64,"Gaibandha"))
        cityArrayList.add(Cityes(65,"Lalmonirhat"))
        cityArrayList.add(Cityes(66,"Kurigram"))
        cityArrayList.add(Cityes(67,"Nawabganj"))

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
                    newImageArray.add(ImageItem(UUID.randomUUID().toString(),imageUri.toString()))
//                    when (i) {
//                        0 -> binding.imagepicker1.setImageURI(imageUri)
//                        1 -> binding.imagepicker2.setImageURI(imageUri)
//                        2 -> binding.imagepicker3.setImageURI(imageUri)
//                        3 -> binding.imagepicker4.setImageURI(imageUri)
//
//                    }
                }

                recylerviewInitalize()
            } else if (data?.data != null) {
                val imageUri: Uri = data.data!!
                imageUris.add(imageUri)
                // Do something with the image (save it to some directory or whatever you need to do with it here)
                // Set image to first ImageView
             //   binding.imagepicker1.setImageURI(imageUri)
            }
        }
    }

    private fun recylerviewInitalize() {
        binding.imageRV.isVisible = true
        val imageAdapter = ImageDetailsSmallViewAdapter(this::onDetailsCliked)
        binding.imageRV.adapter = imageAdapter
        imageAdapter.submitList(newImageArray)
    }

    private fun onDetailsCliked(_id: String, from: String) {

        // show my custom photviewe dialog
        val view = LayoutInflater.from(context).inflate(R.layout.photoviewer, null)
        val builder = AlertDialog.Builder(context)
        builder.setView(view)

        val alert = builder.create()
        alert.setCancelable(true)
        val photoView  = view.findViewById<PhotoView>(R.id.photo_view)

        // load image from server
        Glide
            .with(requireContext())
            .load("https://nir-house-renting-service-65vv8.ondigitalocean.app/uploads/$_id")
            .centerCrop()
            .placeholder(R.drawable.demo_home_photo)
            .into(photoView)


        alert.window?.setBackgroundDrawable(ColorDrawable(0))
        alert.show()

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

        val searchEditText = view.findViewById<AppCompatEditText>(R.id.search_edit_text_bttom)
        val one = view.findViewById<MaterialCardView>(R.id.num1)
        val two = view.findViewById<MaterialCardView>(R.id.num2)
        val three = view.findViewById<MaterialCardView>(R.id.num3)
        val four = view.findViewById<MaterialCardView>(R.id.num4)
        val five = view.findViewById<MaterialCardView>(R.id.num5)



        val save = view.findViewById<TextView>(R.id.saveText)
        save.setOnClickListener {
            setTex(check,searchEditText.text.toString().toInt())
            dialog.dismiss()
        }

        one.setOnClickListener {
           setTex(check,1)

            dialog.dismiss()
        }

        two.setOnClickListener {
            setTex(check,2)
            dialog.dismiss()
        }

        three.setOnClickListener {
            setTex(check,3)
            dialog.dismiss()
        }

        four.setOnClickListener {
            setTex(check,4)
            dialog.dismiss()
        }

        five.setOnClickListener {
            setTex(check,5)
            dialog.dismiss()
        }





        dialog.show()
    }

    private fun setTex(check: String, i: Int) {
        when(check){
            "Beadroom" -> {
                binding.bedroomTextFiled.text = i.toString()
                selectedBeadroom = i

            }
            "Drawingroom" -> {
                binding.drawingroomTextFiled.text = i.toString()
                selectedDrawingroom = i
            }
            "Diningroom" -> {
                binding.diningroomTextFiled.text = i.toString()
                selectedDiningroom = i
            }
            "Bathroom" -> {
                binding.bathroomTextFiled.text = i.toString()
                selectedBathroom = i
            }
            "Kitchen" -> {
                binding.kitchenTextFiled.text = i.toString()
                selectedKitchen = i
            }
            "Balcony" -> {
                binding.balconyTextFiled.text = i.toString()
                selectedBalcony = i
            }
        }
    }



    private fun replaceFragment(fragment: Fragment,bundle: Bundle){
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragment.arguments = bundle
        fragmentTransaction.replace(R.id.fragmentConthainerView4,fragment)

        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()

    }


}