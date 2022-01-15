package com.example.mycoffee.cafelist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mycoffee.R
import com.example.mycoffee.databinding.ActivityAuthenticationBinding
import com.example.mycoffee.databinding.ActivityCafeListBinding
import com.example.mycoffee.displayqr.DisplayQRActivity
import com.example.mycoffee.services.Firebase
import kotlinx.coroutines.flow.collectLatest

class CafeListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCafeListBinding

    private lateinit var viewModel: CafeListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCafeListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(CafeListViewModel::class.java)
        viewModel.getCafes()

        setDefaultValues()
        setObserver()
        setOnClickListener()
    }

    private fun setObserver() {
        lifecycleScope.launchWhenStarted {
            viewModel.cafeList.collectLatest {
                binding.cafeList.adapter = CafeAdapter(it)
            }
        }
    }

    private fun setOnClickListener() {
        binding.fab.setOnClickListener {
            navigateNewScreen(Intent(applicationContext, DisplayQRActivity::class.java))
        }
    }

    private fun setDefaultValues() {
        // todo: base' e taşınabilir
        title = if (!viewModel.getDisplayName().isNullOrEmpty()) {
            "Hoşgeldin " + viewModel.getDisplayName()
        } else {
            "Hoşgeldin"
        }

        binding.cafeList.layoutManager = LinearLayoutManager(this)
        binding.cafeList.setHasFixedSize(true)
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

    fun navigateNewScreen(intent: Intent) {
        startActivity(intent)
        // finish() // Todo: BaseActivitye taşındığında finish() olayı parametre ile kontrol edilebilir
    }
}