package com.example.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.ui.databinding.ActivityStoryDetailBinding

class StoryDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoryDetailBinding

    companion object {
        const val EXTRA_STORY_NAME = "extra_story_detail"
        const val EXTRA_STORY_IMAGE = "extra_story_image"
        const val EXTRA_STORY_DESC = "extra_story_desc"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val storyDetailName = intent.getStringExtra(EXTRA_STORY_NAME)
        val storyDetailImage = intent.getStringExtra(EXTRA_STORY_IMAGE)
        val storyDetailDesc = intent.getStringExtra(EXTRA_STORY_DESC)
        setStoryDetail(storyDetailName!!, storyDetailImage!!, storyDetailDesc!!)
    }

    private fun setStoryDetail(name: String, img: String, desc: String){
        binding.tvNameDetail.text = name
        binding.tvDescriptionDetail.text = desc
        Glide.with(this).load(img).into(binding.imageStoryDetail)
    }
}