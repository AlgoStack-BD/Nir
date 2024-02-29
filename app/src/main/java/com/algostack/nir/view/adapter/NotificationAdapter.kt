package com.algostack.nir.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.algostack.nir.R
import com.algostack.nir.databinding.NotificationItemBinding
import com.algostack.nir.services.model.NotificationData
import com.algostack.nir.services.model.NotificationResponseData
import com.algostack.nir.utils.TokenManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import javax.inject.Inject

class NotificationAdapter (private val onDetailsClickekd: (NotificationResponseData) -> Unit) : ListAdapter<NotificationResponseData, NotificationAdapter.NotificationViewHolder>(ComparatorDiffUtil()) {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding =
            NotificationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class NotificationViewHolder(private val binding: NotificationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(item: NotificationResponseData) {





            if (item.userRead) {
                binding.notificationItemLayout.setBackgroundColor (
                    binding.root.context.resources.getColor(R.color.white)

                )
                binding.unseenSign.isVisible = false
            } else {
                binding.notificationItemLayout.setBackgroundColor(
                    binding.root.context.resources.getColor(R.color.unreadcolor)
                )
            }

            Glide.with(binding.root.context)
                .load("https://nir-house-renting-service-65vv8.ondigitalocean.app/uploads/${item.clientImage}")
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.profileedit)
                .error(R.drawable.profileedit)
                .into(binding.notificationProfile)

            binding.notificationTitle.text = item.clientName
            binding.notificationContent.text =
                "Want to visit your ${item.postTitle} flat on the ${item.meetingDate} at ${item.meetingTime}"

            binding.root.setOnClickListener {
                onDetailsClickekd(item)
            }
        }



    }

    class ComparatorDiffUtil :
        androidx.recyclerview.widget.DiffUtil.ItemCallback<NotificationResponseData>() {
        override fun areItemsTheSame(
            oldItem: NotificationResponseData,
            newItem: NotificationResponseData
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: NotificationResponseData,
            newItem: NotificationResponseData
        ): Boolean {
            return oldItem == newItem
        }
    }


}