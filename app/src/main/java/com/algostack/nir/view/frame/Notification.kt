package com.algostack.nir.view.frame

import android.R
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.algostack.nir.databinding.FragmentNotificationBinding
import com.algostack.nir.services.model.NotificationData
import com.algostack.nir.services.model.NotificationResponseData
import com.algostack.nir.services.model.NotificationUpdateRequest
import com.algostack.nir.utils.NetworkResult
import com.algostack.nir.utils.TokenManager
import com.algostack.nir.view.adapter.NotificationAdapter
import com.algostack.nir.view.adapter.VerticalSpace
import com.algostack.nir.viewmodel.NotificationViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
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
        val gson = Gson()
        val json = gson.toJson(item)
        val bundle = Bundle()
        bundle.putString("notification", json)
//        val postDetails = PostDetails()
//        postDetails.arguments = bundle
//        replaceFragmentGenaral(postDetails, PostDetails::class.java.name)
      notificationViewModel.updateNotification(item._id, NotificationUpdateRequest(NotificationData(true, "pending")))
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
                        notificationAdapter.submitList(

                            it.data.data.filter { notification ->
                                notification.userId != tokenManager.getUserId() || (notification.status != "pending" && notification.userId == tokenManager.getUserId())
                            }

                        )

                       // notificationAdapter.submitList(it.data.data)
                    }else if (it.data.status == 500) {
                        binding.ifNoDataAvilable.isVisible = true
                    }
                }

                is NetworkResult.Error -> {
                   binding.ifNoDataAvilable.isVisible = true
                }
            }
        }
    }


}