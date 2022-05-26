package com.example.ui

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.adapter.LoadingStateAdapter
import com.example.adapter.StoryAdapter
import com.example.helper.Session
import com.example.intermediateandroid.ui.R
import com.example.intermediateandroid.ui.databinding.ActivityHomeBinding
import com.example.viewmodel.HomeViewModel
import com.example.viewmodel.ViewModelFactory

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var session: Session
    private lateinit var swipeRefresh: SwipeRefreshLayout

    private var adapter = StoryAdapter()

    private val viewModel: HomeViewModel by viewModels {
        ViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        session = Session(this)
        binding.progressBar.visibility = View.GONE
        swipeRefresh = binding.refresh
        getData()

        swipeRefresh.setOnRefreshListener {
            swipeRefresh.isRefreshing = true
            getData()
        }
    }

    private fun getData() {
        binding.rvStory.layoutManager = LinearLayoutManager(this)
        binding.rvStory.adapter = adapter
        binding.rvStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        viewModel.story.observe(this) { data ->
            adapter.submitData(lifecycle, data)
            swipeRefresh.isRefreshing = false
        }
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

            R.id.maps -> {
                Intent(this, MapsActivity::class.java).also {
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
            .setPositiveButton("Yes") { _, _ ->
                session.saveLogin(false)
                session.deleteToken()
                Intent(this, LoginActivity::class.java).also {
                    startActivity(it)
                }
                finish()
            }
            .setNegativeButton("No", null)
            .show()
    }
}