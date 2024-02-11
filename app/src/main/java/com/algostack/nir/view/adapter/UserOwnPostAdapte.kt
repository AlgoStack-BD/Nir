package com.algostack.nir.view.adapter

import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.algostack.nir.R
import com.algostack.nir.databinding.EditlayoutItemBinding
import com.algostack.nir.databinding.NearpostBinding
import com.algostack.nir.databinding.PublicPostBinding
import com.algostack.nir.services.model.PublicPostData
import com.algostack.nir.services.model.PublicPostResponse
import com.algostack.nir.services.model.SingleUserResponseData
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class UserOwnPostAdapte (private val onDetailsCliked: (_id: String) -> Unit): ListAdapter<PublicPostData,UserOwnPostAdapte.UserOwnPostViewHolder>(comparatorDiffutil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserOwnPostViewHolder {
        val binding = EditlayoutItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return UserOwnPostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserOwnPostViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)

    }
    inner class UserOwnPostViewHolder(private val binding: EditlayoutItemBinding ) : ViewHolder(binding.root) {

        fun bind(item: PublicPostData){
            binding.rahimHOus.text = item.userName
            binding.sylhetJind.text = item.location
            val fistOneImg: String

            if(item.img.contains(',')){
                fistOneImg = item.img.substringBefore(',')
            }else fistOneImg = item.img


            Glide.with(itemView)
                .load("https://nir-house-renting-service-65vv8.ondigitalocean.app/uploads/$fistOneImg")
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.demo_home_photo)
                .error(R.drawable.demo_home_photo)
                .into(binding.cardImage)


            binding.threedotmenulayout.setOnClickListener {

             PopupMenu(itemView.context, it).apply {
                    setOnMenuItemClickListener { it ->
                        when (it.itemId) {
                            R.id.editpost -> {
                                Toast.makeText(itemView.context, "Edit", Toast.LENGTH_SHORT).show()
                                true
                            }
                            R.id.deletepost -> {
                                Toast.makeText(itemView.context, "Delete", Toast.LENGTH_SHORT).show()

                                onDetailsCliked(item._id)


                                true
                            }
                            else -> false
                        }
                    }
                    inflate(R.menu.threedotmenu)
                    show()
                }
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

