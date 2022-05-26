package com.example.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.example.intermediateandroid.ui.databinding.ItemStoriesBinding
import com.example.network.ListStory
import com.example.ui.StoryDetailActivity

class StoryAdapter : PagingDataAdapter<ListStory, StoryAdapter.StoryViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ItemStoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
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
                itemView.setOnClickListener {
                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            itemView.context as Activity,
                            androidx.core.util.Pair(tvName, "name"),
                            androidx.core.util.Pair(tvDescription, "desc"),
                            androidx.core.util.Pair(imageStory, "image"),
                        )

                    Intent(it.context, StoryDetailActivity::class.java).also { intent ->
                        intent.putExtra(StoryDetailActivity.EXTRA_STORY_NAME, storyList.name)
                        intent.putExtra(StoryDetailActivity.EXTRA_STORY_IMAGE, storyList.photoUrl)
                        intent.putExtra(StoryDetailActivity.EXTRA_STORY_DESC, storyList.description)
                        itemView.context.startActivity(intent, optionsCompat.toBundle())
                    }
                }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStory>() {
            override fun areItemsTheSame(oldItem: ListStory, newItem: ListStory): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStory, newItem: ListStory): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}