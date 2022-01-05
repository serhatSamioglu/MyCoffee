package com.example.mycoffee

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mycoffee.authentication.AuthenticationActivity

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val intent= Intent(this, AuthenticationActivity::class.java)
        startActivity(intent)
        finish()
    }
}