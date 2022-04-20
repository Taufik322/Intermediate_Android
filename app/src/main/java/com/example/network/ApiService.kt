package com.example.network

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
//    @FormUrlEncoded
    @POST("register")
    fun registerUser(
        @Body register: UserRegister
    ): Call<UserRegisterResponse>

    @POST("login")
    fun loginUser(
        @Body login: UserLogin
    ): Call<UserLoginResponse>

    @GET("stories")
    fun getAllStories(
        @Header("Authorization") token: String
    ): Call<Stories>

    @Multipart
    @POST("stories")
    fun uploadStory(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): Call<UploadStoryResponse>
}