package com.algostack.nir.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.algostack.nir.services.model.PublicPostResponse
import com.algostack.nir.services.repository.FiltterRepository
import com.algostack.nir.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(
    private val filterRepository: FiltterRepository

) : ViewModel() {

    val FilterLiveData : LiveData<NetworkResult<PublicPostResponse>>
        get() = filterRepository.FilterLiveData

    fun filter(minPrice: Int, maximumPrice: Int,fixedPrice:Int, bedRoom: Int,bathRoom: Int,propertyType:String, location: String){
       viewModelScope.launch {
           filterRepository.filter(minPrice,maximumPrice,fixedPrice,bedRoom,bathRoom,propertyType,location)
         }

    }

    fun filterbyCategory(category: String){
        viewModelScope.launch {
            filterRepository.filterByBedRoom(category)
        }
    }




}