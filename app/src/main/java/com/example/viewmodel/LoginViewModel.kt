package com.example.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.network.ApiConfig
import com.example.network.DataLoginResult
import com.example.network.UserLogin
import com.example.network.UserLoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {
    val _isSuccessful = MutableLiveData<Boolean>()
    val isSuccessful: LiveData<Boolean> = _isSuccessful

    private val _response = MutableLiveData<DataLoginResult>()
    val response: LiveData<DataLoginResult> = _response

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun setUserLogin(user: UserLogin){
        _isLoading.value = true

        val client = ApiConfig.getApiService().loginUser(user)
        client.enqueue(object : Callback<UserLoginResponse> {
            override fun onResponse(call: Call<UserLoginResponse>, response: Response<UserLoginResponse>) {
                if (response.isSuccessful){
                    _isLoading.value = false
                    _isSuccessful.postValue(true)
                    _response.postValue(response.body()?.loginResult)
                } else {
                    _isSuccessful.postValue(false)
                    _isLoading.value = false
                }
            }

            override fun onFailure(call: Call<UserLoginResponse>, t: Throwable) {
            }
        })
    }
}