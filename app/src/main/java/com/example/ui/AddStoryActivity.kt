package com.example.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ui.databinding.ActivityAddStoryBinding
import com.example.ui.databinding.ItemStoriesBinding

class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}