package com.example.mycoffee.authentication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.mycoffee.R
import com.example.mycoffee.databinding.ActivityAuthenticationBinding
import com.example.mycoffee.services.Firebase

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
            when(viewModel.selectedAuthenticationType.value) {
                SIGN_UP -> viewModel.createUser(this, binding.emailEditText.text.toString(), binding.passwordEditText.text.toString())
                LOG_IN -> viewModel.signInUser(this, binding.emailEditText.text.toString(), binding.passwordEditText.text.toString())
            }
        }
    }

    private fun setDefaultValues() { // TODO: gereksinime göre parametre alan bir fun yapıalbilir
        viewModel.selectedAuthenticationType.postValue(SIGN_UP)
        viewModel.authenticationButtonText.postValue(resources.getString(R.string.sign_up))
    }
}