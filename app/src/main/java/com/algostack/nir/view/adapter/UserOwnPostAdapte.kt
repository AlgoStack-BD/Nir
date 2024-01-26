package com.algostack.nir.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.algostack.nir.R
import com.algostack.nir.databinding.PublicPostBinding
import com.algostack.nir.services.model.PublicPostData
import com.algostack.nir.services.model.PublicPostResponse
import com.algostack.nir.services.model.SingleUserResponseData
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class UserOwnPostAdapte : ListAdapter<PublicPostData,UserOwnPostAdapte.UserOwnPostViewHolder>(comparatorDiffutil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserOwnPostViewHolder {
        val binding = PublicPostBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return UserOwnPostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserOwnPostViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)

    }
    inner class UserOwnPostViewHolder(private val binding: PublicPostBinding) : ViewHolder(binding.root) {

        fun bind(item: PublicPostData){
            binding.publicTitle.text = item.userName
            binding.publicRentPrice.text = item.price.toString()
            binding.numOfPublicBadroom.text = item.bedRoom.toString()
            binding.numOfPublicBathroom.text = item.bathRoom.toString()

            val fistOneImg: String

            if(item.img.contains(',')){
                fistOneImg = item.img.substringBefore(',')
            }else fistOneImg = item.img


            Glide.with(itemView)
                .load(fistOneImg)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.demo_home_photo)
                .error(R.drawable.demo_home_photo)
                .into(binding.publicView)



        }


    }
    class comparatorDiffutil : DiffUtil.ItemCallback<PublicPostData>(){
        override fun areItemsTheSame(oldItem: PublicPostData, newItem: PublicPostData): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: PublicPostData, newItem: PublicPostData): Boolean {
            return oldItem == newItem
        }
    }


}