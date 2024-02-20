package com.algostack.nir.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.algostack.nir.R
import com.algostack.nir.databinding.EditlayoutItemBinding
import com.algostack.nir.databinding.FavouritelistitemBinding
import com.algostack.nir.services.model.PublicPostData
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class UserOwnFavouriteListAdapter (private val onDetailsCliked: (_id: String,from: String) -> Unit): ListAdapter<PublicPostData, UserOwnFavouriteListAdapter.UserOwnFavouriteListViewHolder>(comparatorDiffutil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserOwnFavouriteListViewHolder {
        val binding = FavouritelistitemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return UserOwnFavouriteListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserOwnFavouriteListViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)

    }
    inner class UserOwnFavouriteListViewHolder(private val binding: FavouritelistitemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PublicPostData){
            binding.rahimHOus.text = item.userName
            binding.sylhetJind.text = item.location
            val fistOneImg: String

            if(item.img!!.contains(',')){
                fistOneImg = item.img.substringBefore(',')
            }else fistOneImg = item.img


            Glide.with(itemView)
                .load("https://nir-house-renting-service-65vv8.ondigitalocean.app/uploads/$fistOneImg")
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.demo_home_photo)
                .error(R.drawable.demo_home_photo)
                .into(binding.cardImage)



            binding.removeFavourite.setOnClickListener {
                item._id?.let { it1 -> onDetailsCliked(it1,"removefavourite") }
            }


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

