package com.example.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.network.ApiConfig
import com.example.network.UserLogin
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {
    private val _isSuccessful = MutableLiveData<Boolean>()
    val isSuccessful: LiveData<Boolean> = _isSuccessful

    fun setUserLogin(user: UserLogin){
        val client = ApiConfig.getApiService().loginUser(user)
        client.enqueue(object : Callback<UserLogin> {
            override fun onResponse(call: Call<UserLogin>, response: Response<UserLogin>) {
                _isSuccessful.value = response.isSuccessful
            }

            override fun onFailure(call: Call<UserLogin>, t: Throwable) {

            }

        })
    }
}