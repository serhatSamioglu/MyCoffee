package com.example.mycoffee

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mycoffee.authentication.AuthenticationActivity
import com.example.mycoffee.cafelist.CafeListActivity
import com.example.mycoffee.services.Firebase
import java.lang.Boolean.FALSE
import java.lang.Boolean.TRUE

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        when(Firebase.isCurrentUser()){
            TRUE -> {
                navigateNewScreen(Intent(this, CafeListActivity::class.java))
            }
            FALSE -> {
                navigateNewScreen(Intent(this, AuthenticationActivity::class.java))
            }
        }
    }

    fun navigateNewScreen(intent: Intent) { // Todo: BaseActivitye taşınabilir
        startActivity(intent)
        finish()
    }
}