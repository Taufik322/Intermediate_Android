package com.example.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.customview.ButtomCustomLogin
import com.example.customview.EditTextCustomPassword
import com.example.helper.Session
import com.example.network.ApiService
import com.example.network.UserLogin
import com.example.ui.databinding.ActivityLoginBinding
import com.example.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var myEditTextPassword: EditTextCustomPassword
    private lateinit var errorMessage: View
    private lateinit var loginButton: ButtomCustomLogin
    private lateinit var binding: ActivityLoginBinding

    private lateinit var viewModel: LoginViewModel
    private lateinit var session: Session

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            LoginViewModel::class.java
        )

//        val pref = Session.getInstance(dataStore)
//        val viewModelSetting = ViewModelProvider(this, ViewModelFactory(pref)).get(HomeViewModel::class.java)

        myEditTextPassword = findViewById(R.id.edit_text_password)
        errorMessage = findViewById(R.id.tv_password_error_message)
        loginButton = findViewById(R.id.button_login)

        showLoading(false)
        binding.buttonLogin.isEnabled = false

        session = Session(this)

        myEditTextPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().length < 6)  {
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
        }

        viewModel.isSuccessful.observe(this) {
            if (it) {
                loginProcess()
//                viewModelSetting.saveUserSession(true)
                viewModel.response.observe(this) { dataLogin ->

                    session.saveToken(dataLogin.token)
//                    TOKEN = dataLogin.token
                    makeToast(session.getToken()!!)
                    session.saveLogin(true)
                }
//                makeToast(Session.TOKEN)
            } else {
                makeToast("Wrong email or password!")
            }
        }

        viewModel.isLoading.observe(this){
            showLoading(it)
        }

        binding.tvSignup.setOnClickListener {
            Intent(this, SignupActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    private fun loginProcess() {
        Intent(this, HomeActivity::class.java).also {
            startActivity(it)
        }
        finish()
//        val intent = Intent(this, HomeActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        startActivity(intent)
//        session.setLogin(true)
    }

    private fun makeToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(value: Boolean){
        if (value){
            binding.progressBar.visibility = View.VISIBLE
            binding.buttonLogin.isEnabled = false
            binding.tvSignup.isEnabled = false

            binding.logoLogin.alpha = 0.5f
            binding.tvEmail.alpha = 0.5f
            binding.editTextEmail.alpha = 0.5f
            binding.tvPassword.alpha = 0.5f
            binding.editTextPassword.alpha = 0.5f
            binding.tvSignup.alpha = 0.5f
        } else {
            binding.progressBar.visibility = View.GONE
            binding.buttonLogin.isEnabled = true
            binding.tvSignup.isEnabled = true

            binding.logoLogin.alpha = 1f
            binding.tvEmail.alpha = 1f
            binding.editTextEmail.alpha = 1f
            binding.tvPassword.alpha = 1f
            binding.editTextPassword.alpha = 1f
            binding.tvSignup.alpha = 1f
        }
    }

    override fun onStart() {
        super.onStart()
        if (session.getLogin()){
            Intent(this, HomeActivity::class.java).also {
                startActivity(it)
            }
            finish()
        }
    }
}