package com.example.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("register")
    fun registerUser(
        @Body register: UserRegister
    ): Call<UserRegister>

    @POST("login")
    fun loginUser(
        @Body login: UserLogin
    ): Call<UserLoginResponse>

    @GET("stories")
    fun getAllStories(
        @Header("Authorization") token: String
    ): Call<Stories>
}