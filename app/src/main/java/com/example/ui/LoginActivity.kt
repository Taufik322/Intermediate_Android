package com.example.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import com.example.customview.ButtomCustomLogin
import com.example.customview.EditTextCustomPassword
import kotlin.math.sign

class LoginActivity : AppCompatActivity() {
    private lateinit var myEditTextPassword: EditTextCustomPassword
    private lateinit var errorMessage: View
    private lateinit var loginButton: ButtomCustomLogin
    private lateinit var signup: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

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

        signup.setOnClickListener {
            Intent(this, SignupActivity::class.java).also {
                startActivity(it)
            }
        }
    }
}