package com.example.mycoffee

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import com.example.mycoffee.services.Firebase

class CafeListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cafe_list)
        setTitle("Hoşgeldin Serhat") // todo: base' e taşınabilir
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Firebase.signOutUser()
        Toast.makeText(this, "Option Pressed", Toast.LENGTH_SHORT).show()
        return super.onOptionsItemSelected(item)
    }
}