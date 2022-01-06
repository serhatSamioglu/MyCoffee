package com.example.mycoffee.authentication

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mycoffee.services.Firebase

class AuthenticationViewModel: ViewModel() {

    var selectedAuthenticationType = MutableLiveData<Int>()
    var authenticationButtonText = MutableLiveData<String>()

    companion object {
        const val SIGN_UP = 0
        const val LOG_IN = 1
    }

    fun createUser(context: Context, email: String, password: String) {
        Firebase.createUser(context, email, password)
    }

    fun signInUser(context: Context, email: String, password: String) {
        Firebase.signIn(context, email, password)
    }
}