package com.algostack.nir.view.frame

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.scale
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager

import com.algostack.nir.R
import com.algostack.nir.databinding.FragmentInteriorAiwithGEMINIBinding
import com.algostack.nir.services.model.Message
import com.algostack.nir.utils.Constants.RECEIVE_ID
import com.algostack.nir.utils.Constants.SEND_ID
import com.algostack.nir.utils.Time
import com.algostack.nir.view.adapter.MessagingAdapter
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.FileDescriptor
import java.io.IOException
import javax.inject.Inject


@AndroidEntryPoint
class InteriorAiwithGEMINI : Fragment() {


    companion object {

        private const val PERMISSION_REQUEST_CODE = 100
        private const val REQUEST_OPEN_GALLERY = 2
        private const val STORAGE_PERMISSION_CODE = 23


    }

    private var _binding : FragmentInteriorAiwithGEMINIBinding?= null
    private val binding get() = _binding!!

    var messagesList = mutableListOf<Message>()

    private lateinit var adapter: MessagingAdapter
    private val botList = listOf("GPT", "Bard", "Egal", "Iron")
   private val API_KEY = "AIzaSyAHvKjoJqQd2VB5NaYUh9Hb0nfw0U1dJdc"
    private val MODEL_NAME = "gemini-pro-vision" // Adjust based on your Gemini project
    var bitmap: Bitmap? = null
    private var prompt = ""

    @Inject
    lateinit var time: Time

    private var imageUris : Uri ?= null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

            _binding = FragmentInteriorAiwithGEMINIBinding.inflate(inflater,container,false)

        setupBackPress()
            return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.intorioraiphoto.setOnClickListener {
            if(checkPermission()){
                openGalleryForImage()

                // startGalleryIntent()
            }else{
                requestPermission()
                println("checkPermission: ${checkPermission()}")
                Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()

            }
        }


        recyclerView()

        clickEvents()

        val random = (0..3).random()
        customBotMessage("Hello! Today you're speaking with ${botList[random]}, how may I help?")
    }

    private fun clickEvents() {

        //Send a message
        binding.btnSend.setOnClickListener {
            sendMessage()
        }

        //Scroll back to correct position when user clicks on text view
        binding.etMessage.setOnClickListener {
            GlobalScope.launch {
                delay(100)

                withContext(Dispatchers.Main) {
                    binding.rvMessages.scrollToPosition(adapter.itemCount - 1)

                }
            }


        }
    }

    private fun recyclerView() {
        adapter = MessagingAdapter()
        binding.rvMessages.adapter = adapter

        binding.rvMessages.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)

    }

    override fun onStart() {
        super.onStart()
        //In case there are messages, scroll to bottom when re-opening app
        GlobalScope.launch {
            delay(100)
            withContext(Dispatchers.Main) {
                binding.rvMessages.scrollToPosition(adapter.itemCount - 1)
            }
        }
    }

    private fun sendMessage() {
        val message = binding.etMessage.text.toString()
         prompt = message
        val timeStamp = time.timeStamp()

        if (message.isNotEmpty()) {

            //Adds it to our local list
            messagesList.add(Message(message, SEND_ID, imageUris!!))
            binding.etMessage.setText("")
            // Set default image to second ImageView
            binding.intorioraiphoto.setImageResource(R.drawable.addphoto)

            adapter.insertMessage(Message(message, SEND_ID, imageUris!!))
            binding.rvMessages.scrollToPosition(adapter.itemCount - 1)


            if (bitmap != null) {
              println("prompt $prompt -> bitmap $bitmap")

                sendImageAndTextPrompt(prompt, bitmap!!)
            }

        }
    }
/*
    private fun botResponse() {
        val timeStamp = time.timeStamp()

        GlobalScope.launch {
            //Fake response delay
            delay(1000)

            withContext(Dispatchers.Main) {
                //Gets the response

                val generativeModel = GenerativeModel(
                    // For text-and-images input (multimodal), use the gemini-pro-vision model
                    modelName = "gemini-pro-vision",
                    // Access your API key as a Build Configuration variable (see "Set up your API key" above)
                    apiKey = API_KEY
                )


                val inputContent = content {
                    image(bitmap!!)
                    text(prompt)
                }

               // val response = generativeModel.generateContent(inputContent)
                val response = try {
                    generativeModel.generateContent(inputContent)
                } catch (e: Exception) {
                    // Handle errors here
                    println("Error generating response: $e")
                    // Return a default response or display an error message
                    null
                }
                print( "geminiresponse"+response?.text)


                //val response = BotResponse.basicResponses(message)

                //Adds it to our local list
                messagesList.add(Message(response.toString(), RECEIVE_ID, timeStamp))

                //Inserts our message into the adapter
                adapter.insertMessage(Message(response.toString(), RECEIVE_ID, timeStamp))

                //Scrolls us to the position of the latest message
                binding.rvMessages.scrollToPosition(adapter.itemCount - 1)


            }
        }
    }
*/

    private fun sendImageAndTextPrompt(prompt: String, bitmap: Bitmap) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val generativeModel = GenerativeModel(
                    modelName = MODEL_NAME,
                    apiKey = API_KEY
                )

                val inputContent = content {
                    image(bitmap)
                    text(prompt)
                }

                val response = generativeModel.generateContent(inputContent)
                val textResponse = response.text



                // Add bot response to local list and adapter
                textResponse?.let { Message(it, RECEIVE_ID, imageUris!! ) }
                    ?.let { messagesList.add(it) }
                textResponse?.let { Message(it, RECEIVE_ID, imageUris!!) }
                    ?.let { adapter.insertMessage(it) }
                binding.rvMessages.scrollToPosition(adapter.itemCount - 1)

            } catch (e: Exception) {
                Log.e("GeminiError", "Error sending image and text prompt: $e")
                // Handle error gracefully (e.g., display a user-friendly message)
            }
        }
    }
    private fun customBotMessage(message: String) {

        GlobalScope.launch {
            delay(1000)
            withContext(Dispatchers.Main) {
                val timeStamp = time.timeStamp()
                messagesList.add(Message(message, RECEIVE_ID, Uri.EMPTY))
                adapter.insertMessage(Message(message, RECEIVE_ID, Uri.EMPTY))

                binding.rvMessages.scrollToPosition(adapter.itemCount - 1)
            }
        }
    }

    private fun openGalleryForImage() {





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
                imageUris = imageUri
               uriToBitmap(imageUri)
                // Do something with the image (save it to some directory or whatever you need to do with it here)
                // Set image to first ImageView
                binding.intorioraiphoto.setImageURI(imageUri)
            }
        }
    }

    //TODO takes URI of the image and returns bitmap
    private fun uriToBitmap(selectedFileUri: Uri) {


        try {
            val stream = requireActivity().contentResolver.openInputStream(selectedFileUri)
            if (stream != null) {
                val options = BitmapFactory.Options()
                options.inSampleSize = 4 // Adjust downsampling as needed
                bitmap = BitmapFactory.decodeStream(stream, null, options)
                stream.close()
            }
        } catch (e: Exception) {
            Log.e("ImageDecodeError", "Error decoding image:", e)
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
                    fragmentTransaction.remove(this@InteriorAiwithGEMINI)

                }


            }
        })


    }
}

