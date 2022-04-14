package com.example.network

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserLogin(
    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("password")
    val password: String
) : Parcelable
