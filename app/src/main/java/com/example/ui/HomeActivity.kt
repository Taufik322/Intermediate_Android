package com.example.ui

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.adapter.StoryAdapter
import com.example.helper.Session
import com.example.network.ListStory
import com.example.ui.databinding.ActivityHomeBinding
import com.example.viewmodel.HomeViewModel

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var session: Session

    private lateinit var adapter: StoryAdapter
    private lateinit var viewModel: HomeViewModel

    private lateinit var swipeRefresh: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        session = Session(this)
        binding.progressBar.visibility = View.GONE
        swipeRefresh = binding.refresh

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            HomeViewModel::class.java
        )

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        adapter = StoryAdapter()

        viewModel.getAllStories(session.getToken().toString())
        viewModel.listStory.observe(this) {
            if (it != null) {
                setStoryData(it)
            }
        }

        swipeRefresh.setOnRefreshListener {
            viewModel.getAllStories(session.getToken().toString())
            viewModel.listStory.observe(this) {
                if (it != null) {
                    setStoryData(it)
                }
            }
            swipeRefresh.isRefreshing = false
        }
    }

    private fun setStoryData(data: List<ListStory>) {
        adapter.setStoryList(data)
        binding.rvStory.layoutManager = LinearLayoutManager(this)
        binding.rvStory.setHasFixedSize(true)
        binding.rvStory.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.log_out -> {
                alertDialogLogout()
                return true
            }

            R.id.add_story -> {
                Intent(this, AddStoryActivity::class.java).also {
                    startActivity(it)
                }
            }
        }
        return true
    }

    private fun alertDialogLogout() {
        AlertDialog.Builder(this)
            .setTitle("Log Out")
            .setMessage("Are you sure want to log out?")
            .setPositiveButton("Yes", DialogInterface.OnClickListener { _, _ ->
                session.saveLogin(false)
                session.deleteToken()
                Intent(this, LoginActivity::class.java).also {
                    startActivity(it)
                }
                finish()
            })
            .setNegativeButton("No", null)
            .show()
    }

    private fun showLoading(value: Boolean){
        if (value) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}