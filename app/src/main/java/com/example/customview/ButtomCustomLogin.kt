package com.example.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.example.intermediateandroid.ui.R

class ButtomCustomLogin : AppCompatButton {
    private lateinit var enabledLoginButton: Drawable
    private lateinit var disabledLoginButton: Drawable

    constructor(context: Context) : super(context){
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        background = if(isEnabled) enabledLoginButton else disabledLoginButton
        textSize = 18f
    }

    private fun init(){
        enabledLoginButton = ContextCompat.getDrawable(context, R.drawable.bg_button_login) as Drawable
        disabledLoginButton = ContextCompat.getDrawable(context, R.drawable.bg_button_login_disabled) as Drawable
    }
}