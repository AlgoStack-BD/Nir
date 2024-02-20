package com.algostack.nir.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.algostack.nir.R
import com.algostack.nir.databinding.MessageItemBinding
import com.algostack.nir.services.model.Message
import com.algostack.nir.utils.Constants.RECEIVE_ID
import com.algostack.nir.utils.Constants.SEND_ID
class MessagingAdapter : RecyclerView.Adapter<MessagingAdapter.MessageViewHolder>() {

    var messagesList = mutableListOf<Message>()

    inner class MessageViewHolder(private val binding: MessageItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                // Remove message on the item clicked
                messagesList.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)
            }
        }

        fun bind(message: Message) {
            when (message.id) {
                SEND_ID -> {
                    binding.tvMessage.apply {
                        text = message.message
                        visibility = View.VISIBLE
                    }
                    binding.tvBotMessage.visibility = View.GONE
                }
                RECEIVE_ID -> {
                    binding.tvBotMessage.apply {
                        text = message.message
                        visibility = View.VISIBLE
                    }
                    binding.tvMessage.visibility = View.GONE
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val binding = MessageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MessageViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return messagesList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val item = messagesList[position]
        holder.bind(item)
    }

    fun insertMessage(message: Message) {
        messagesList.add(message)
        notifyItemInserted(messagesList.size - 1)
    }
}