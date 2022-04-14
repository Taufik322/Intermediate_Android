package com.example.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.customview.ButtomCustomLogin
import com.example.customview.EditTextCustomPassword
import com.example.network.ApiConfig
import com.example.network.UserRegister
import com.example.ui.databinding.ActivitySignupBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupActivity : AppCompatActivity() {
    private lateinit var myEditTextPassword: EditTextCustomPassword
    private lateinit var signupButton: ButtomCustomLogin
    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

            val client = ApiConfig.getApiService().registerUser(regUser)
            client.enqueue(object : Callback<UserRegister> {
                override fun onResponse(call: Call<UserRegister>, response: Response<UserRegister>) {
                    if (response.isSuccessful){
                        makeToast("User Created!, Please login")
//                        Intent(this@SignupActivity, LoginActivity::class.java).also {
//                            startActivity(it)
//                        }
//                        Log.e("TAG", name)
                    } else {
//                        makeToast(false, response.message())
                        Log.e("TAG", "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<UserRegister>, t: Throwable) {
                    Log.e("TAG", "onFailure: ${t.message}")
                }
            })
        }

        binding.tvSignup.setOnClickListener {
            Intent(this, LoginActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    private fun makeToast(msg: String){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }


}