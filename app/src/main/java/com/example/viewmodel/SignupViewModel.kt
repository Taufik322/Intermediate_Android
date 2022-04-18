package com.example.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.network.ApiConfig
import com.example.network.UserRegister
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupViewModel : ViewModel() {
    private val _isSuccessful = MutableLiveData<Boolean>()
    val isSuccessful: LiveData<Boolean> = _isSuccessful

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _response = MutableLiveData<String>()
    val response: LiveData<String> = _response

    fun setUserSignup(userRegister: UserRegister){
        _isLoading.value = true

        val client = ApiConfig.getApiService().registerUser(userRegister)
        client.enqueue(object : Callback<UserRegister> {
            override fun onResponse(call: Call<UserRegister>, response: Response<UserRegister>) {
                if (response.isSuccessful){
                    _isLoading.value = false
                    _isSuccessful.value = true
                    _response.postValue(response.message())
                } else {
                    _isLoading.value = false
                    _isSuccessful.value = false
                    _response.postValue(response.message())
                }
            }

            override fun onFailure(call: Call<UserRegister>, t: Throwable) {
                Log.e("TAG", "onFailure: ${t.message}")
            }
        })
    }
}