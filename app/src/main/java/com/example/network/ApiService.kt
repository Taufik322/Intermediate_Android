package com.example.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

//    @FormUrlEncoded
    @POST("register")
    fun registerUser(
        @Body register: UserRegister
    ): Call<UserRegister>

    @POST("login")
    fun loginUser(
        @Body login: UserLogin
    ): Call<UserLoginResponse>

    @GET("login")
    fun loginResponse(
//        @Body login: UserLogin
//        @Query("error") error: String,
//        @Query("message") message: String,
//        @Query("loginResult") loginResult: List<DataLoginResult>
    ) : Call<UserLoginResponse>
}