package com.algostack.nir.view.frame

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.algostack.nir.R
import com.algostack.nir.databinding.FragmentNotificationBinding
import com.algostack.nir.utils.NetworkResult
import com.algostack.nir.utils.TokenManager
import com.algostack.nir.view.adapter.NotificationAdapter
import com.algostack.nir.view.adapter.VerticalSpace
import com.algostack.nir.viewmodel.NotificationViewModel
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import dagger.hilt.android.AndroidEntryPoint
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
        notificationAdapter = NotificationAdapter()
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        println("tptoken = ${tokenManager.getUserId()!!}")
        notificationViewModel.getNotifications(tokenManager.getUserId()!!)


        binding.notificationRV.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL,false)
        binding.notificationRV.addItemDecoration(bestForYouRecSpace)
        binding.notificationRV.adapter = notificationAdapter


        bindObservers()



    }

    private fun bindObservers() {
        notificationViewModel.userNotifications.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Loading -> {

                }

                is NetworkResult.Success -> {

                    if (it.data!!.status == 200) {
                        notificationAdapter.submitList(it.data.data)
                    }
                }

                is NetworkResult.Error -> {

                }
            }
        }
    }


}