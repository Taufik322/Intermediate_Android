package com.example.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.example.customview.ButtomCustomLogin
import com.example.customview.EditTextCustomPassword
import kotlin.math.sign

class SignupActivity : AppCompatActivity() {
    private lateinit var myEditTextPassword: EditTextCustomPassword
    private lateinit var errorMessage: View
    private lateinit var signupButton: ButtomCustomLogin


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        myEditTextPassword = findViewById(R.id.edit_text_password_signup)
        errorMessage = findViewById(R.id.tv_password_error_message_signup)
        signupButton = findViewById(R.id.button_signup)

        myEditTextPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().length < 6) {
                    errorMessage.visibility = View.VISIBLE
                    signupButton.isEnabled = false
                } else {
                    errorMessage.visibility = View.INVISIBLE
                    signupButton.isEnabled = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }


}