package com.example.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.customview.ButtomCustomLogin
import com.example.customview.EditTextCustomPassword
import com.example.network.ApiConfig
import com.example.network.UserLogin
import com.example.network.UserRegister
import com.example.ui.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.sign

class LoginActivity : AppCompatActivity() {
    private lateinit var myEditTextPassword: EditTextCustomPassword
    private lateinit var errorMessage: View
    private lateinit var loginButton: ButtomCustomLogin
    private lateinit var signup: TextView
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

            val client = ApiConfig.getApiService().loginUser(loginUser)
            client.enqueue(object : Callback<UserLogin> {
                override fun onResponse(call: Call<UserLogin>, response: Response<UserLogin>) {
                    if(response.isSuccessful){
                        makeToast(response.body().toString())
                        Intent(this@LoginActivity, HomeActivity::class.java).also {
                            startActivity(it)
                        }
                    }else{
                        makeToast(response.message())
                    }
                }

                override fun onFailure(call: Call<UserLogin>, t: Throwable) {

                }

            })
        }

        signup.setOnClickListener {
            Intent(this, SignupActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    private fun makeToast(msg: String){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}