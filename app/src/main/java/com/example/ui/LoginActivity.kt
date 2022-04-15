package com.example.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.customview.ButtomCustomLogin
import com.example.customview.EditTextCustomPassword
import com.example.network.UserLogin
import com.example.ui.databinding.ActivityLoginBinding
import com.example.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var myEditTextPassword: EditTextCustomPassword
    private lateinit var errorMessage: View
    private lateinit var loginButton: ButtomCustomLogin
    private lateinit var signup: TextView
    private lateinit var binding: ActivityLoginBinding

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            LoginViewModel::class.java
        )

        myEditTextPassword = findViewById(R.id.edit_text_password)
        errorMessage = findViewById(R.id.tv_password_error_message)
        loginButton = findViewById(R.id.button_login)
        signup = findViewById(R.id.tv_signup)

        myEditTextPassword.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().length < 6) {
                    errorMessage.visibility = View.VISIBLE
                    loginButton.isEnabled = false
                } else {
                    errorMessage.visibility = View.INVISIBLE
                    loginButton.isEnabled = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        binding.buttonLogin.setOnClickListener {
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()
            val loginUser = UserLogin(email, password)

            viewModel.setUserLogin(loginUser)
            viewModel.isSuccessful.observe(this) {
                loginProcess(it)
            }
        }

        signup.setOnClickListener {
            Intent(this, SignupActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    private fun loginProcess(value: Boolean){
        if (value) {
            Intent(this, MainActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    private fun makeToast(msg: String){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}