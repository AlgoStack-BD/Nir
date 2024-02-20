package com.algostack.nir.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.algostack.nir.R
import com.algostack.nir.databinding.NearpostBinding
import com.algostack.nir.services.model.PublicPostData
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class PublicFeedNearByPostAdapter : ListAdapter<PublicPostData, PublicFeedNearByPostAdapter.NearForYouViewHolder>(ComparatorDiffUtil()) {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NearForYouViewHolder {
        val binding = NearpostBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return NearForYouViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NearForYouViewHolder, position: Int) {

        val item = getItem(position)
        holder.bind(item)

    }

    inner class NearForYouViewHolder(private val binding: NearpostBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: PublicPostData){
            binding.rahimHOus.text = item.title
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

        }

    }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<PublicPostData>() {
        override fun areItemsTheSame(oldItem: PublicPostData, newItem: PublicPostData): Boolean {

            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: PublicPostData, newItem: PublicPostData): Boolean {
            return oldItem == newItem
        }
    }

}