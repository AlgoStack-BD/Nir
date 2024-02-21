package com.algostack.nir.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.algostack.nir.databinding.NotificationItemBinding
import com.algostack.nir.services.model.NotificationData
import com.algostack.nir.services.model.NotificationResponseData

class NotificationAdapter : ListAdapter<NotificationResponseData, NotificationAdapter.NotificationViewHolder>(
    NotificationAdapter.ComparatorDiffUtil()
){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding = NotificationItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class NotificationViewHolder(private val binding: NotificationItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: NotificationResponseData){
            binding.notificationTitle.text = item.postTitle
        }
    }

    class ComparatorDiffUtil : androidx.recyclerview.widget.DiffUtil.ItemCallback<NotificationResponseData>(){
        override fun areItemsTheSame(oldItem: NotificationResponseData, newItem: NotificationResponseData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: NotificationResponseData, newItem: NotificationResponseData): Boolean {
            return oldItem == newItem
        }
    }


}