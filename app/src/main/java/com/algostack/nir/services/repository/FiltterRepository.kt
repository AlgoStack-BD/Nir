package com.algostack.nir.services.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.algostack.nir.services.db.NirLocalDB
import com.algostack.nir.services.model.PublicPostResponse
import com.algostack.nir.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FiltterRepository @Inject constructor(
    private  val nirLocalDB: NirLocalDB
){

    private val _FilterLiveData = MutableLiveData<NetworkResult<PublicPostResponse>> ()
    val FilterLiveData : MutableLiveData<NetworkResult<PublicPostResponse>>
        get() = _FilterLiveData


   suspend fun filter(
       minPrice: Int, maximumPrice: Int,
       fixedPrice: Int,
       bedRoom: Int,
       bathRoom: Int,
       propertyType: String,
       location: String){


       Log.d("SearchResult", "filterShow: $minPrice $maximumPrice $fixedPrice $bedRoom $bathRoom $propertyType $location")

       withContext(Dispatchers.IO){
           val list = nirLocalDB.getPublicPostDao().searchItems(minPrice,maximumPrice,fixedPrice,bedRoom,bathRoom,propertyType,location)
           Log.d("SearchResult", "filter: $list")

           val response = nirLocalDB.getPublicPostDao().getPublicPostData()
           //Log.d("SearchResult", "publicPost: $response")

           _FilterLiveData.postValue(NetworkResult.Success((PublicPostResponse(list,200))))
           _FilterLiveData.postValue(NetworkResult.Error("No Data Found"))
         }


    }


    suspend fun filterByBedRoom(bedRoom: Int){
        Log.d("SearchResult", "filterShow: $bedRoom")
        withContext(Dispatchers.IO){
            val list = nirLocalDB.getPublicPostDao().searchItemsByBedRoom(bedRoom)
            Log.d("SearchResult", "filter: $list")
            _FilterLiveData.postValue(NetworkResult.Success((PublicPostResponse(list,200))))
            _FilterLiveData.postValue(NetworkResult.Error("No Data Found"))
        }
    }




}