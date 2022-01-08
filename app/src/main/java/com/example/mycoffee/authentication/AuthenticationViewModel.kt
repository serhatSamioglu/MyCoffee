package com.example.mycoffee.authentication

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mycoffee.services.Firebase
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class AuthenticationViewModel: ViewModel() {

    var selectedAuthenticationType = MutableLiveData<Int>() // todo: stateflow a geçilebilir
    var authenticationButtonText = MutableLiveData<String>() // todo: stateflow a geçilebilir

    private val _authenticationResponse = MutableSharedFlow<AuthResult?>()
    val authenticationResponse = _authenticationResponse.asSharedFlow()

    companion object {
        const val SIGN_UP = 0
        const val LOG_IN = 1
    }

    fun createUser(activity: Activity, email: String, password: String) {
        GlobalScope.launch(Dispatchers.Main) {
            _authenticationResponse.emit(Firebase.createUser(activity, email, password))
        }
    }

    fun signInUser(activity: Activity, email: String, password: String) {
        GlobalScope.launch(Dispatchers.Main) {
            _authenticationResponse.emit(Firebase.signIn(activity, email, password))
        }
    }
}