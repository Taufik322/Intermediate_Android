package com.example.network

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.helper.Session
import com.example.ui.LoginActivity
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
//    val tokens : String = LoginActivity.TOKEN
//    val session: Session

//    fun getTok(): String?{
//        return session.getToken()
//    }


    //    @FormUrlEncoded
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