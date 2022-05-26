package com.example.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.network.ApiConfig
import com.example.network.ListStory
import com.example.network.Stories
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsViewModel: ViewModel() {

    private val _listStory = MutableLiveData<List<ListStory>>()
    val listStory: LiveData<List<ListStory>> = _listStory

    fun getLocation(token: String){
        val client = ApiConfig.getApiService().getAllStoriesWithLocation("Bearer $token", 1)
        client.enqueue(object : Callback<Stories> {
            override fun onResponse(call: Call<Stories>, response: Response<Stories>) {
                if (response.isSuccessful){
                    _listStory.postValue(response.body()?.listStory)
                }
            }

            override fun onFailure(call: Call<Stories>, t: Throwable) {
            }
        })
    }
}