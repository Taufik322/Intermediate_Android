package com.example.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.helper.Session
import com.example.ui.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var session: Session

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        session = Session(this)

        binding.btnLogout.setOnClickListener{
            session.saveLogin(false)
            Intent(this, LoginActivity::class.java).also {
                startActivity(it)
            }
            finish()
        }
    }
}