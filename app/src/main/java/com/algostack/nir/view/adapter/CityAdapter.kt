package com.algostack.nir.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.algostack.nir.R
import com.algostack.nir.databinding.FragmentSelectCityBinding
import com.algostack.nir.services.model.Cityes

class CityAdapter(private val cityList : ArrayList<Cityes>, private val onItemClick: (Cityes) -> Unit) : RecyclerView.Adapter<CityAdapter.CityViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val viewLayout =
            LayoutInflater.from(parent.context).inflate(R.layout.city_name_item, parent, false)

        return CityViewHolder(viewLayout)
    }

    override fun getItemCount() = cityList.size


    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        val currentItem = cityList[position]
        holder.foodName.text = currentItem.cityName

        holder.itemView.setOnClickListener {
            onItemClick(currentItem)
        }

    }



    inner  class CityViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)
    {

        val foodName : TextView = itemView.findViewById(R.id.cityName)
    }
}








