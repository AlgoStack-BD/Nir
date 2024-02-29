package com.algostack.nir.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.algostack.nir.R
import com.algostack.nir.services.model.Cityes
import com.algostack.nir.services.model.Numbers
import java.util.Locale

class NumbersAdapter (private val originalCityList: ArrayList<Numbers>, private val onItemClick: (Numbers) -> Unit) :
    RecyclerView.Adapter<NumbersAdapter.CityViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val viewLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.city_name_item, parent, false)

        return CityViewHolder(viewLayout)
    }

    override fun getItemCount() = originalCityList.size

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        val currentItem = originalCityList[position]
        holder.foodName.text = currentItem.cityName

        holder.itemView.setOnClickListener {
            onItemClick(currentItem)
        }
    }


    fun setFilter(query: String) {
        println("query: $query")
        val filteredList = ArrayList<Numbers>()

        if(query.isNotBlank()){ // if query is not empty
            for (i in originalCityList) {
                if (i.cityName.toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT))) {
                    filteredList.add(i)
                }
            }

            updateList(filteredList)
        }

    }


    inner class CityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val foodName: TextView = itemView.findViewById(R.id.cityName)
    }

    fun updateList(newList: List<Numbers>) {

        println("newList: $newList")

        originalCityList.clear()
        originalCityList.addAll(newList)
        notifyDataSetChanged()
    }

}