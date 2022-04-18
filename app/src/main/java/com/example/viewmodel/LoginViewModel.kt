package com.example.viewmodel

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.network.ApiConfig
import com.example.network.DataLoginResult
import com.example.network.UserLogin
import com.example.network.UserLoginResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {
    val _isSuccessful = MutableLiveData<Boolean>()
    val isSuccessful: LiveData<Boolean> = _isSuccessful

    private val _isSuccessful2 = MutableLiveData<ResponseBody>()
    val isSuccessful2: LiveData<ResponseBody> = _isSuccessful2

    private val _response = MutableLiveData<DataLoginResult>()
    val response: LiveData<DataLoginResult> = _response

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun setUserLogin(user: UserLogin){
//        _isSuccessful.value = false
        _isLoading.value = true

        val client = ApiConfig.getApiService().loginUser(user)
        client.enqueue(object : Callback<UserLoginResponse> {
            override fun onResponse(call: Call<UserLoginResponse>, response: Response<UserLoginResponse>) {
                if (response.isSuccessful){
                    _isLoading.value = false
                    _isSuccessful.postValue(true)
                    _response.postValue(response.body()?.loginResult)
//                    _isSuccessful2.postValue(response.errorBody())
//                    _response.postValue(response.message())
                } else {
//                    _isSuccessful2.postValue(response.errorBody())
                    _isSuccessful.postValue(false)
                    _isLoading.value = false
//                    _response.postValue(response.message())
                }
            }

            override fun onFailure(call: Call<UserLoginResponse>, t: Throwable) {
            }
        })
    }
}