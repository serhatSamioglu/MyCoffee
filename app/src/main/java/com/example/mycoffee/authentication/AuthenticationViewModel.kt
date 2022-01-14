package com.example.mycoffee.authentication

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mycoffee.services.Firebase
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthenticationViewModel: ViewModel() {

    var selectedAuthenticationType = MutableLiveData<Int>() // todo: stateflow a geçilebilir
    var authenticationButtonText = MutableLiveData<String>() // todo: stateflow a geçilebilir

    private val _createUserResponse = MutableSharedFlow<AuthResult?>()
    val createUserResponse = _createUserResponse.asSharedFlow()

    private val _signInResponse = MutableSharedFlow<AuthResult?>()
    val signInResponse = _signInResponse.asSharedFlow()

    companion object {
        const val SIGN_UP = 0
        const val LOG_IN = 1
    }

    fun createUser(activity: Activity, email: String, password: String) {
        viewModelScope.launch() {
            _createUserResponse.emit(Firebase.createUser(activity, email, password))
        }
    }

    fun signIn(activity: Activity, email: String, password: String) {
        viewModelScope.launch {
            _signInResponse.emit(Firebase.signIn(activity, email, password))
        }
    }

    fun sendUserInfoToDatabase(email: String, fullName: String, password: String) {
        Firebase.sendUserInfoToDatabase(email, fullName, password)
    }
}