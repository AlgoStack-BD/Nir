package com.algostack.nir.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.algostack.nir.R
import com.algostack.nir.databinding.SmallImageItemLayoutBinding
import com.algostack.nir.services.model.ImageItem
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class ImageDetailsSmallViewAdapter : ListAdapter<ImageItem, ImageDetailsSmallViewAdapter.ImageDetailsSmallViewHolder>(Diffcallback())
{


    class Diffcallback : DiffUtil.ItemCallback<ImageItem>(){
        override fun areItemsTheSame(oldItem: ImageItem, newItem: ImageItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ImageItem, newItem: ImageItem): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImageDetailsSmallViewAdapter.ImageDetailsSmallViewHolder {
        val binding = SmallImageItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ImageDetailsSmallViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ImageDetailsSmallViewAdapter.ImageDetailsSmallViewHolder,
        position: Int
    ) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ImageDetailsSmallViewHolder(private val binding: SmallImageItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ImageItem){

            Glide.with(itemView)
                .load(item.url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.demo_home_photo)
                .error(R.drawable.demo_home_photo)
                .into(binding.imageView)
        }


    }


}