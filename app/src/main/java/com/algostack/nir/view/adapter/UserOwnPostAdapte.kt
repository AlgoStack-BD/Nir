package com.algostack.nir.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.algostack.nir.services.model.SingleUserResponseData

class UserOwnPostAdapte {


    class comparatorDiffutil : DiffUtil.ItemCallback<SingleUserResponseData>(){
        override fun areItemsTheSame(oldItem: SingleUserResponseData, newItem: SingleUserResponseData): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: SingleUserResponseData, newItem: SingleUserResponseData): Boolean {
            return oldItem == newItem
        }
    }
}