package com.example.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.customview.ButtomCustomLogin
import com.example.customview.EditTextCustomPassword
import com.example.intermediateandroid.ui.R
import com.example.intermediateandroid.ui.databinding.ActivitySignupBinding
import com.example.network.UserRegister
import com.example.viewmodel.SignupViewModel

class SignupActivity : AppCompatActivity() {
    private lateinit var myEditTextPassword: EditTextCustomPassword
    private lateinit var signupButton: ButtomCustomLogin
    private lateinit var binding: ActivitySignupBinding

    private lateinit var viewModel: SignupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
           SignupViewModel::class.java
        )

        showLoading(false)
        binding.buttonSignup.isEnabled = false

        myEditTextPassword = findViewById(R.id.edit_text_password_signup)
        signupButton = findViewById(R.id.button_signup)

        myEditTextPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().length < 6) {
                    binding.tvPasswordErrorMessageSignup.visibility = View.VISIBLE
                    signupButton.isEnabled = false
                } else {
                    binding.tvPasswordErrorMessageSignup.visibility = View.INVISIBLE
                    signupButton.isEnabled = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        binding.buttonSignup.setOnClickListener {
            val name = binding.editTextNameSignup.text.toString().trim()
            val email = binding.editTextEmailSignup.text.toString().trim()
            val password = binding.editTextPasswordSignup.text.toString().trim()
            val regUser = UserRegister(name, email, password)

            viewModel.setUserSignup(regUser)
        }

        viewModel.isSuccessful.observe(this) {
            viewModel.response.observe(this) { response ->
                if(it){
                    signupProcess(it)
                } else {
                    makeToast("Must be valid email!")
                }
            }
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        binding.tvLogin.setOnClickListener {
            Intent(this, LoginActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }
    }

    private fun signupProcess(value: Boolean) {
        if (value) {
            makeToast("User Created!, Please login")
        }
    }

    private fun makeToast(msg: String){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(value: Boolean){
        binding.apply {
            progressBar.visibility = if(value) View.VISIBLE else View.GONE
            buttonSignup.isEnabled = !value
            tvLogin.isEnabled = !value

            val logoAlpha = if (value) 0.5f else 1f
            logoLogin.alpha = logoAlpha
            tvNameSignup.alpha = logoAlpha
            editTextNameSignup.alpha = logoAlpha
            tvEmailSignup.alpha = logoAlpha
            editTextEmailSignup.alpha = logoAlpha
            tvPasswordSignup.alpha = logoAlpha
            editTextPasswordSignup.alpha = logoAlpha
        }
    }

}