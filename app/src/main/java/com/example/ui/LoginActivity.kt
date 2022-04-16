package com.example.ui

import android.content.ContentValues.TAG
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
import com.example.helper.Session
import com.example.network.DataLoginResult
import com.example.network.UserLogin
import com.example.ui.databinding.ActivityLoginBinding
import com.example.viewmodel.LoginViewModel
import kotlin.math.log

class LoginActivity : AppCompatActivity() {
    private lateinit var myEditTextPassword: EditTextCustomPassword
    private lateinit var errorMessage: View
    private lateinit var loginButton: ButtomCustomLogin
    private lateinit var signup: TextView
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

        myEditTextPassword = findViewById(R.id.edit_text_password)
        errorMessage = findViewById(R.id.tv_password_error_message)
        loginButton = findViewById(R.id.button_login)
        signup = findViewById(R.id.tv_signup)

        session = Session(this)

        myEditTextPassword.addTextChangedListener(object : TextWatcher {
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
            makeToast("Please Wait")

        }

//        viewModel._isSuccessful.value = false
//
//        viewModel.isSuccessful.observe(this) {
//            if (it) {
//                loginProcess(it)
//                viewModel.response.observe(this) { dataLogin ->
//                    makeToast(dataLogin.token)
//                }
//            }
//        }

        viewModel.isSuccessful.observe(this) {
            if (it) {
                loginProcess()
                viewModel.response.observe(this) { dataLogin ->
                    session.saveToken(dataLogin.token)
                    session.saveLogin(true)
                }
            } else {
                makeToast("salah")
            }
        }


        signup.setOnClickListener {
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


    override fun onStart() {
        super.onStart()
        if (session.getLogin()){
//            val intent = Intent(this, HomeActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            startActivity(intent)
//            finish()

            Intent(this, HomeActivity::class.java).also {
                startActivity(it)
            }
            finish()
        }
    }
}