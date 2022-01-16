package com.example.mycoffee.cafedetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.mycoffee.databinding.ActivityCafeDetailBinding
import com.example.mycoffee.dataclass.CafeListItem

class CafeDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCafeDetailBinding

    private lateinit var viewModel: CafeDetailViewModel

    // private lateinit var cafeListItem: CafeListItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // add databinding
        binding = ActivityCafeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // add viewmodel
        viewModel = ViewModelProvider(this).get(CafeDetailViewModel::class.java)
        // two-way için eklendi sanırım :)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        viewModel.cafeListItem.value = getExtras()
    }

    private fun getExtras(): CafeListItem {
        return intent.getParcelableExtra("CafeListItem")!!
    }
}