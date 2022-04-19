package com.example.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.network.ApiConfig
import com.example.network.ListStory
import com.example.network.Stories
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeViewModel() : ViewModel(){

    private val _listStoryResponse = MutableLiveData<Stories>()
    val listStoryResponse: LiveData<Stories> = _listStoryResponse

    private val _listStory = MutableLiveData<List<ListStory>>()
    val listStory: LiveData<List<ListStory>> = _listStory

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getAllStories(token: String){
        _isLoading.value = true

        val client = ApiConfig.getApiService().getAllStories("Bearer $token")
        client.enqueue(object : Callback<Stories> {
            override fun onResponse(call: Call<Stories>, response: Response<Stories>) {
                if (response.isSuccessful){
                    if (response != null){
                        _isLoading.value = false
                        _listStoryResponse.postValue(response.body())
                        _listStory.postValue(response.body()?.listStory)

                        Log.d(ContentValues.TAG, "onResponse: ${response.body()}")
                    }
                } else {
                    _isLoading.value = false
                }
            }

            override fun onFailure(call: Call<Stories>, t: Throwable) {
                Log.d(ContentValues.TAG, "testtt: $call")
            }
        })
    }
}

//class HomeViewModel(private val pref: Session) : ViewModel() {
//    fun getUserSession(): LiveData<Boolean> {
//        return pref.getSession().asLiveData()
//    }
//
//    fun saveUserSession(isLoggedIn: Boolean) {
//        viewModelScope.launch {
//            pref.saveSession(isLoggedIn)
//        }
//    }
//}