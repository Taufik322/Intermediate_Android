package com.example.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.example.network.ListStory
import com.example.ui.databinding.ItemStoriesBinding

//import com.example.ui.databinding.ActivityHomeBinding
//import com.example.ui.databinding.ItemStoriesBinding

class StoryAdapter : RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {
    private val story = ArrayList<ListStory>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val storyItem =
            ItemStoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(storyItem)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = story[position]
        holder.bind(story)
    }

    override fun getItemCount(): Int {
        return story.size
    }

    class StoryViewHolder(private val binding: ItemStoriesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(storyList: ListStory) {
            with(binding) {
                tvName.text = storyList.name
                tvDescription.text = storyList.description
                Glide.with(itemView.context)
                    .load(storyList.photoUrl)
                    .transition(withCrossFade())
                    .into(imageStory)
            }
        }
    }

    fun setStoryList(storyList: List<ListStory>) {
        story.addAll(storyList)
    }
}