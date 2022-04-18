package com.example.viewmodel

import android.content.ContentValues
import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.example.helper.Session
import com.example.network.ApiConfig
import com.example.network.ListStory
import com.example.network.Stories
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeViewModel() : ViewModel(){

    private val _listStory = MutableLiveData<Stories>()
    val listStory: LiveData<Stories> = _listStory

    private val _listStory2 = MutableLiveData<List<ListStory>>()
    val listStory2: LiveData<List<ListStory>> = _listStory2

    fun getAllStories(token: String){
        val client = ApiConfig.getApiService().getAllStories("Bearer $token")
        client.enqueue(object : Callback<Stories> {
            override fun onResponse(call: Call<Stories>, response: Response<Stories>) {
                if (response.isSuccessful){
                    if (response != null){
//                        storyList = response.body()
//                        adapter.setStoryList(response.body())
                        _listStory.postValue(response.body())
                        _listStory2.postValue(response.body()?.listStory)

                        Log.d(ContentValues.TAG, "onResponse: ${response.body()}")
                    }
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