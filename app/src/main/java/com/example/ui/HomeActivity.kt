package com.example.ui

import android.app.AlertDialog
import android.app.SearchManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adapter.StoryAdapter
import com.example.helper.Session
import com.example.network.ListStory
import com.example.ui.databinding.ActivityHomeBinding
import com.example.viewmodel.HomeViewModel

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var session: Session

    private lateinit var adapter: StoryAdapter
    private lateinit var storList: List<ListStory>

    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        session = Session(this)

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            HomeViewModel::class.java
        )


        adapter = StoryAdapter()

        viewModel.getAllStories(session.getToken().toString())
        viewModel.listStory2.observe(this) {
            if (it != null){
                adapter.setStoryList(it)
                binding.rvStory.layoutManager = LinearLayoutManager(this)
                binding.rvStory.setHasFixedSize(true)
                binding.rvStory.adapter = adapter
            }
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
        }
        return true
    }

    private fun alertDialogLogout(){
        AlertDialog.Builder(this)
            .setTitle("Log Out")
            .setMessage("Are you sure want to log out?")
            .setPositiveButton("Yes", DialogInterface.OnClickListener{ _, _ ->
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
    private fun makeToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}