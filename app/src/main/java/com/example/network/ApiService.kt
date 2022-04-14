package com.example.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

//    @FormUrlEncoded
    @POST("register")
    fun registerUser(
        @Body register: UserRegister
    ): Call<UserRegister>

    @POST("login")
    fun loginUser(
        @Body login: UserLogin
    ): Call<UserLogin>
}