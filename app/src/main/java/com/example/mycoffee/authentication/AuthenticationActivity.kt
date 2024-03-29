package com.example.mycoffee.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.mycoffee.cafelist.CafeListActivity
import com.example.mycoffee.R
import com.example.mycoffee.activitylist.ActivityListActivity
import com.example.mycoffee.databinding.ActivityAuthenticationBinding
import com.example.mycoffee.scanqr.ScanQRActivity
import com.example.mycoffee.services.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AuthenticationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthenticationBinding

    private lateinit var viewModel: AuthenticationViewModel

    companion object {
        const val SIGN_UP = 0
        const val LOG_IN = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // add databinding
        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // add viewmodel
        viewModel = ViewModelProvider(this).get(AuthenticationViewModel::class.java)
        // two-way için eklendi
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        setDefaultValues()
        setListener()
        setObserver()
    }

    private fun setListener() {
        binding.buttonToggleGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.signUpButton -> {
                        viewModel.selectedAuthenticationType.postValue(SIGN_UP)
                        viewModel.authenticationButtonText.postValue(resources.getString(R.string.sign_up))
                    }
                    R.id.loginButton -> {
                        viewModel.selectedAuthenticationType.postValue(LOG_IN)
                        viewModel.authenticationButtonText.postValue(resources.getString(R.string.login))
                    }
                }
            }
        }

        binding.authenticationButton.setOnClickListener {
            when (viewModel.selectedAuthenticationType.value) {
                SIGN_UP -> viewModel.createUser(this, binding.emailEditText.text.toString(), binding.passwordEditText.text.toString())
                LOG_IN -> viewModel.signIn(this, binding.emailEditText.text.toString(), binding.passwordEditText.text.toString())
            }
        }
    }

    private fun setObserver() {
        GlobalScope.launch {
            viewModel.createUserResponse.collectLatest {
                it?.let {
                    navigateNewScreen(Intent(applicationContext, CafeListActivity::class.java)) // this yapamadığım için applicationContext verdim
                    viewModel.sendUserInfoToDatabase(binding.emailEditText.text.toString(), binding.fullNameEditText.text.toString(), binding.passwordEditText.text.toString())
                    // kullanıcını shared da tutmak gerekiyor.
                    // CafeListActivitye üye olup gelince firebase den çekmek uzun sürüyor
                    // putextra ile taşımadım çünkü başka activityde de lazım olabilir
                    // shareda null çek yapmak lazım silinme veya ulaşılmama ihtimaline karşı
                }
            }
        }

        GlobalScope.launch {
            viewModel.signInResponse.collectLatest {
                it?.let {
                    when (Firebase.getRole()) {
                        "customer" -> navigateNewScreen(Intent(this@AuthenticationActivity, CafeListActivity::class.java))
                        "cashier" -> navigateNewScreen(Intent(this@AuthenticationActivity, ScanQRActivity::class.java))
                        "employer" -> navigateNewScreen(Intent(this@AuthenticationActivity, ActivityListActivity::class.java))
                        // rolu null ise ne yapilacagina karar verilecek
                    }
                }
            }
        }
    }

    private fun setDefaultValues() { // TODO: gereksinime göre parametre alan bir fun yapıalbilir
        viewModel.selectedAuthenticationType.postValue(SIGN_UP)
        viewModel.authenticationButtonText.postValue(resources.getString(R.string.sign_up))
    }

    fun navigateNewScreen(intent: Intent) { // Todo: BaseActivitye taşınabilir
        startActivity(intent)
        finish()
    }
}