package com.example.mycoffee.authentication

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AuthenticationViewModel: ViewModel() {

    var selectedAuthenticationType = MutableLiveData<Int>()
    var authenticationButtonText = MutableLiveData<String>()

    companion object {
        const val SIGN_UP = 0
        const val LOG_IN = 1
    }
}