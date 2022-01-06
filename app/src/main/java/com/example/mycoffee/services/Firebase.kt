package com.example.mycoffee.services

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

object Firebase : Activity() {

    private var auth: FirebaseAuth = FirebaseAuth.getInstance() // laneinit olmadı

    /*init {
        auth = FirebaseAuth.getInstance() // Todo: Firebase.auth' u önermedi
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // auth = FirebaseAuth.getInstance() // Todo: Firebase.auth' u önermedi
    }

    fun createUser(context: Context, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("firebaseLog ", "createUserWithEmail:success")
                    val user = auth.currentUser

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("firebaseLog ", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(context, "Authentication failed." + task.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun signIn(context: Context, email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("firebaseLog", "signInWithEmail:success")
                    val user = auth.currentUser

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("firebaseLog", "signInWithEmail:failure", task.exception)
                    Toast.makeText(context, "Authentication failed." + task.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun isCurrentUser(): Boolean {
        return auth.currentUser != null
    }

    fun signOutUser() {
        auth.signOut()
    }
}