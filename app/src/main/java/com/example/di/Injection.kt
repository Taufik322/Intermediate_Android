package com.example.di

import android.content.Context
import com.example.data.StoryRepository
import com.example.network.ApiConfig

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val apiService = ApiConfig.getApiService()
        return StoryRepository(apiService, context)
    }
}