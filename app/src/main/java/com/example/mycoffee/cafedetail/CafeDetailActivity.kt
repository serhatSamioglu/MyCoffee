package com.example.mycoffee.cafedetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.mycoffee.databinding.ActivityCafeDetailBinding
import com.example.mycoffee.dataclass.CafeListItem
import com.squareup.picasso.Picasso

class CafeDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCafeDetailBinding

    private lateinit var viewModel: CafeDetailViewModel

    private lateinit var cafeListItem: CafeListItem

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
        downloadCafeLogo()
    }

    private fun getExtras(): CafeListItem {
        cafeListItem = intent.getParcelableExtra("CafeListItem")!!
        return cafeListItem
    }

    private fun downloadCafeLogo() {
        Picasso.get().load(cafeListItem.cafe?.logo).fit().centerInside().into(binding.logo)
    }
}