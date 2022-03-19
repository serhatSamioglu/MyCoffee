package com.example.mycoffee

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mycoffee.activitylist.ActivityListActivity
import com.example.mycoffee.authentication.AuthenticationActivity
import com.example.mycoffee.cafelist.CafeListActivity
import com.example.mycoffee.scanqr.ScanQRActivity
import com.example.mycoffee.services.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Boolean.FALSE
import java.lang.Boolean.TRUE

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        GlobalScope.launch {
            when (Firebase.isCurrentUser()) {
                TRUE -> {
                    when (Firebase.getRole()) {
                        "customer" -> navigateNewScreen(Intent(this@SplashScreenActivity, CafeListActivity::class.java))
                        "cashier" -> navigateNewScreen(Intent(this@SplashScreenActivity, ScanQRActivity::class.java))
                        "employer" -> navigateNewScreen(Intent(this@SplashScreenActivity, ActivityListActivity::class.java))
                        // else -> navigateNewScreen(Intent(this@SplashScreenActivity, CafeListActivity::class.java))
                        // rolu null ise ne yapilacagina karar verilecek
                    }
                }
                FALSE -> navigateNewScreen(Intent(this@SplashScreenActivity, AuthenticationActivity::class.java))
            }
        }
    }

    fun navigateNewScreen(intent: Intent) { // Todo: BaseActivitye taşınabilir
        startActivity(intent)
        finish()
    }
}