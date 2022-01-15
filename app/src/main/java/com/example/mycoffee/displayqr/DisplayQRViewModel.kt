package com.example.mycoffee.displayqr

import androidx.lifecycle.ViewModel
import com.example.mycoffee.services.Firebase

class DisplayQRViewModel: ViewModel() {

    fun getCurrentUserID(): String? {
        return Firebase.getCurrentUserID()
    }
}