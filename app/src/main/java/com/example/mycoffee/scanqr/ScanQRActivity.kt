package com.example.mycoffee.scanqr

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.mycoffee.R
import com.example.mycoffee.authentication.AuthenticationActivity
import com.example.mycoffee.databinding.ActivityScanQrBinding
import com.example.mycoffee.dataclass.Reward
import com.example.mycoffee.services.Firebase
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException

class ScanQRActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScanQrBinding

    private var qrScanIntegrator: IntentIntegrator? = null

    private var lastClickedButton: Int? = null
    private var cafeID = MutableStateFlow<String?>(null)
    private var customerReward = MutableSharedFlow<Reward?>()
    private var requiredStar: Int? = null
    private lateinit var customerID: String
    private var cashierID: String? = null

    companion object {
        const val GIVE_STAR = 0
        const val USE_GIFT = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // add databinding
        binding = ActivityScanQrBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // binding.lifecycleOwner = this

        cashierID = Firebase.getCurrentUserID()
        getCafeInformation()
        setupScanner()
        setOnClickListener()
        setObserver()
    }

    private fun getCafeInformation() {
        GlobalScope.launch {
            cafeID.value = Firebase.getCafeID()
        }
        GlobalScope.launch {
            cafeID.collect { cafeID ->
                cafeID?.let { cafe_ID ->
                    Firebase.getCashierCafeRequiredStar(cafe_ID)?.let { requiredStar = it }
                }
            }
        }
    }

    private fun setupScanner() {
        qrScanIntegrator = IntentIntegrator(this)
    }

    private fun setOnClickListener() {
        binding.giveStarButton.setOnClickListener {
            qrScanIntegrator?.initiateScan()
            lastClickedButton = GIVE_STAR
        }
        binding.useGiftButton.setOnClickListener {
            qrScanIntegrator?.initiateScan()
            lastClickedButton = USE_GIFT
        }
    }

    private fun setObserver() {
        GlobalScope.launch(Dispatchers.Main) {
            customerReward.collect { customer_reward ->
                customer_reward?.let {
                    requiredStar?.let { required_star ->
                        when (lastClickedButton) {
                            GIVE_STAR -> Firebase.giveStar(customerID, cafeID.value.toString(), required_star, customer_reward, cashierID)
                            USE_GIFT -> {
                                if (customer_reward.giftCount != 0) {
                                    Firebase.useGift(customerID, cafeID.value.toString(), customer_reward.giftCount, cashierID)
                                } else {
                                    Toast.makeText(this@ScanQRActivity, "hediye kahve yok", Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            // If QRCode has no data.
            if (result.contents == null) {
                Toast.makeText(this, "sonuç yok", Toast.LENGTH_LONG).show()
            } else {
                // If QRCode contains data.
                try {
                    customerID = result.contents.toString()
                    GlobalScope.launch {
                        customerReward.emit(Firebase.getCustomerReward(customerID, cafeID.value.toString()))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    // Data not in the expected format. So, whole object as toast message.
                    Toast.makeText(this, result.contents, Toast.LENGTH_LONG).show()
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Firebase.signOutUser()
        navigateNewScreen(Intent(applicationContext, AuthenticationActivity::class.java))
        finish()
        return super.onOptionsItemSelected(item)
    }

    private fun navigateNewScreen(intent: Intent) {
        startActivity(intent)
        // finish() // Todo: BaseActivitye taşındığında finish() olayı parametre ile kontrol edilebilir
    }
}