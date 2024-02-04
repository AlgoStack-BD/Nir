package com.algostack.nir.view.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.algostack.nir.R
import com.algostack.nir.databinding.PublicPostBinding
import com.algostack.nir.services.model.PublicPostData
import com.algostack.nir.view.frame.PostDetails
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class PublicFeedBestForYouAdapter (private val onDetailsCliked: (PublicPostData) -> Unit ): ListAdapter<PublicPostData, PublicFeedBestForYouAdapter.BestForYouViewHolder> (ComparatorDiffUtil()) {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestForYouViewHolder {
        val binding = PublicPostBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return BestForYouViewHolder(binding)
    }


    override fun onBindViewHolder(holder: BestForYouViewHolder, position: Int) {

        val item = getItem(position)
        holder.bind(item)

    }

    inner class BestForYouViewHolder(private val binding: PublicPostBinding) : ViewHolder(binding.root){



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
                .load("https://nir-house-renting-service-65vv8.ondigitalocean.app/uploads/$fistOneImg")
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.demo_home_photo)
                .error(R.drawable.demo_home_photo)
                .into(binding.publicView)



                binding.root.setOnClickListener{
                    onDetailsCliked(item)
                }





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