package com.algostack.nir.view.frame

import android.R
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.algostack.nir.databinding.FragmentNotificationBinding
import com.algostack.nir.services.model.ImageItem
import com.algostack.nir.services.model.NotificationData
import com.algostack.nir.services.model.NotificationResponseData
import com.algostack.nir.services.model.NotificationUpdateRequest
import com.algostack.nir.services.model.RentRequestData
import com.algostack.nir.services.model.RentRequestNotification
import com.algostack.nir.utils.NetworkResult
import com.algostack.nir.utils.TokenManager
import com.algostack.nir.view.adapter.NotificationAdapter
import com.algostack.nir.view.adapter.VerticalSpace
import com.algostack.nir.viewmodel.NotificationViewModel
import com.algostack.nir.viewmodel.PublicPostViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import de.hdodenhof.circleimageview.CircleImageView
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import javax.inject.Inject


@AndroidEntryPoint
class Notification : Fragment() {


    private var _binding : FragmentNotificationBinding ?= null
    private  val binding get() = _binding!!


    @Inject
    lateinit var tokenManager: TokenManager


    val bestForYouRecSpace = VerticalSpace()
    private lateinit var notificationAdapter: NotificationAdapter

     private val notificationViewModel by viewModels<NotificationViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentNotificationBinding.inflate(inflater,container,false)
        notificationAdapter = NotificationAdapter(this::onDetailsClickekd)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        println("tptoken = ${tokenManager.getUserId()!!}")
        notificationViewModel.getallNotifications()


        binding.notificationRV.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL,false)
        binding.notificationRV.addItemDecoration(bestForYouRecSpace)
        binding.notificationRV.adapter = notificationAdapter

        // swiped left item and delete this item
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val item = notificationAdapter.currentList[position]
                println("itemidcheck = ${item._id}")
                notificationViewModel.deleteNotification(item._id)
                deletebindingOvserver()
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                RecyclerViewSwipeDecorator.Builder(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                    .addBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            com.algostack.nir.R.color.delete
                        )
                    )
                    .addActionIcon(com.algostack.nir.R.drawable.delete)
                    .create()
                    .decorate()

                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
        })
        itemTouchHelper.attachToRecyclerView(binding.notificationRV)

        bindObservers()




    }

  fun onDetailsClickekd(item: NotificationResponseData) {

      if (!item.userRead) {
          notificationViewModel.updateNotification(
              item._id,
              NotificationUpdateRequest(NotificationData(true, item.status))
          )
          notificationViewModel.updateNotificationResponse.observe(viewLifecycleOwner) {
              when (it) {
                  is NetworkResult.Loading -> {

                  }

                  is NetworkResult.Success -> {


                      println("Notification.updateNotificationResponse.observe: it = ${it.data!!.data.matchedCount}")
                      notificationViewModel.getallNotifications()
                      bindObservers()

                  }

                  is NetworkResult.Error -> {

                  }
              }
          }

      }
      else if (item.status == "accepted") {
          val view = LayoutInflater.from(context).inflate(com.algostack.nir.R.layout.acceptorrejectdialoag, null)
          val builder = AlertDialog.Builder(context)
          builder.setView(view)

          val alert = builder.create()
          alert.setCancelable(true)

          val map = view.findViewById<Button>(com.algostack.nir.R.id.gotodirection)



          map.setOnClickListener {
              directionFromCurrentMap(item.location)
          }


          alert.window?.setBackgroundDrawable(ColorDrawable(0))
          alert.show()
      }else if (item.status == "rejected"){
            val view = LayoutInflater.from(context).inflate(com.algostack.nir.R.layout.acceptorrejectdialoag, null)
            val builder = AlertDialog.Builder(context)
            builder.setView(view)

          val img = view.findViewById<ImageView>(com.algostack.nir.R.id.acceptedicon)
            img.setImageResource(com.algostack.nir.R.drawable.rejecticon)

          val button = view.findViewById<Button>(com.algostack.nir.R.id.gotodirection)

          button.visibility = View.GONE


            val alert = builder.create()
            alert.setCancelable(true)
            alert.show()
      }else
      {
          showSchedulegDialog(requireContext(),item.meetingDate, item.meetingTime, item.postTitle, item.postId, item._id )
      }
    }


    // direction map
    private fun directionFromCurrentMap(destinationLatitude: String) {
        // Create a Uri from an intent string. Open map using intent to show direction from current location (latitude, longitude) to specific location (latitude, longitude)
//        val mapUri = Uri.parse("https://maps.google.com/maps?daddr=$destinationLatitude,$destinationLongitude")

        val mapUri = Uri.parse("https://maps.google.com/maps?daddr=$destinationLatitude")
        val intent = Intent(Intent.ACTION_VIEW, mapUri)
        startActivity(intent)
    }
    // Review scheduling function

    fun showSchedulegDialog(context: Context, dateS: String, timeS: String, titleS: String, postId: String, notificationId: String) {
        println("showBookingDialog: done")
        val view = LayoutInflater.from(context).inflate(com.algostack.nir.R.layout.reviewtourrequest, null)
        val builder = AlertDialog.Builder(context)
        builder.setView(view)

        val alert = builder.create()
        alert.setCancelable(true)

        val date = view.findViewById<TextView>(com.algostack.nir.R.id.tourRequestDate)
        val time = view.findViewById<TextView>(com.algostack.nir.R.id.tourRequestTimeValue)
        val title = view.findViewById<TextView>(com.algostack.nir.R.id.tourRequestTitle)
        val button = view.findViewById<TextView>(com.algostack.nir.R.id.viewDetails)
        val decline = view.findViewById<TextView>(com.algostack.nir.R.id.declineSchedule)
        val accept = view.findViewById<TextView>(com.algostack.nir.R.id.acceptSchedule)

        date.setText(dateS)
        time.setText(timeS)
        title.setText(titleS)

         button.setOnClickListener {
             println("postId = $postId")
             notificationViewModel.getSingleNotification(postId)
             notificationViewModel.signlePostResponse.observe(viewLifecycleOwner) {
                 when (it) {
                     is NetworkResult.Loading -> {

                     }

                     is NetworkResult.Success -> {
                         Toast.makeText(requireContext(), "Notification post${it.data!!.data}", Toast.LENGTH_SHORT).show()
                         val data = it.data!!.data
                         /// json bundle and send to the next fragment
                            val gson = Gson()
                            val json = gson.toJson(data)
                            val bundle = Bundle()
                            bundle.putString("details", json)
                            bundle.putString("DestinationPage", "Notification")
                            replaceFragment(PostDetails(),bundle)
                     }

                     is NetworkResult.Error -> {

                     }
                 }
             }

        }

        decline.setOnClickListener {

            notificationViewModel.updateNotification(notificationId, NotificationUpdateRequest(NotificationData(false, "rejected")))
            alert.dismiss()
        }
        accept.setOnClickListener {
            notificationViewModel.updateNotification(notificationId, NotificationUpdateRequest(NotificationData(false, "accepted")))
            alert.dismiss()

        }

        alert.window?.setBackgroundDrawable(ColorDrawable(0))
        alert.show()
    }


    // navigate fragment function
    private fun replaceFragment(fragment: Fragment,bundle: Bundle){
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragment.arguments = bundle
        fragmentTransaction.replace(com.algostack.nir.R.id.fragmentConthainerView4,fragment)

        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()

    }
    private fun deletebindingOvserver() {
        notificationViewModel.deleteNotificationResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Loading -> {

                }

                is NetworkResult.Success -> {


                    if (it.data!!.status == 200 ) {
                        Toast.makeText(requireContext(), "Notification Deleted ${it.data.data.deletedCount}", Toast.LENGTH_SHORT).show()
                        notificationViewModel.getNotifications(tokenManager.getUserId()!!)
                    }
                }

                is NetworkResult.Error -> {

                }
            }
        }
    }

    private fun bindObservers() {
        notificationViewModel.userNotifications.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Loading -> {
                }

                is NetworkResult.Success -> {


                    if (it.data!!.status == 200 && it.data.data.isNotEmpty()  ) {

                        val filteredList = it.data.data.filter { notification ->
                            notification.userId != tokenManager.getUserId() || (notification.userId == tokenManager.getUserId() && notification.status != "pending")
                        }
                        notificationAdapter.submitList(
                            filteredList
                        )


                        notificationAdapter.submitList(filteredList)



                        // check notifyadapter list is empty or not
                        if (notificationAdapter.currentList.isEmpty()) {
                            binding.ifNoDataAvilable.isVisible = true
                        } else {
                            binding.ifNoDataAvilable.isVisible = false
                            binding.notificationRV.isVisible = true
                        }


                       // notificationAdapter.submitList(it.data.data)
                    }
                }

                is NetworkResult.Error -> {
                   binding.ifNoDataAvilable.isVisible = true
                }
            }
        }
    }


}