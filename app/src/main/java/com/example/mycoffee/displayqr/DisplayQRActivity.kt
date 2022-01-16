package com.example.mycoffee.displayqr

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.mycoffee.R
import com.example.mycoffee.databinding.ActivityDisplayQrBinding
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder

class DisplayQRActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDisplayQrBinding

    private lateinit var viewModel: DisplayQRViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // add databinding
        binding = ActivityDisplayQrBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // add viewmodel
        viewModel = ViewModelProvider(this).get(DisplayQRViewModel::class.java)
        // two-way için eklendi
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        setDefaultValues()
        displayQRCode()
    }

    private fun setDefaultValues() {
        title = getString(R.string.scan_qr)
    }

    private fun displayQRCode() {
        viewModel.getCurrentUserID()?.let {
            val bitmap: Bitmap = BarcodeEncoder().encodeBitmap(it, BarcodeFormat.QR_CODE, 800, 800)
            binding.qrCode.setImageBitmap(bitmap)
        }
    }
}