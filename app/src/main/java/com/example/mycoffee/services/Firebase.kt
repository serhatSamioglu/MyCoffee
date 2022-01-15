package com.example.mycoffee.services

import android.app.Activity
import android.widget.Toast
import com.example.mycoffee.dataclass.User
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import com.google.firebase.auth.UserProfileChangeRequest

object Firebase {// FirebaseRepository

    private var auth: FirebaseAuth = FirebaseAuth.getInstance() // laneinit olmadı

    private var starsRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("Stars")

    private var usersRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")

    private var cafesRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("Cafes")

    /*init {
        auth = FirebaseAuth.getInstance() // Todo: Firebase.auth' u önermedi
    }*/

    suspend fun createUser(activity: Activity, email: String, password: String): AuthResult? {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
        }catch (e: Exception){
            Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
            null
        }
    }

    suspend fun signIn(activity: Activity, email: String, password: String): AuthResult? {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
        }catch (e: Exception){
            Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
            null
        }
    }

    fun isCurrentUser(): Boolean {
        return auth.currentUser != null
    }

    fun signOutUser() {
        auth.signOut()
    }

    fun getCurrentUserID(): String? {
        return auth.currentUser?.uid //todo currentuser singleton icinde tutulabilir
    }

    suspend fun getStars(): DataSnapshot? {
        return try {
            getCurrentUserID()?.let { starsRef.child(it).get().await() }
        }catch (e: Exception) {
            null
        }
    }

    suspend fun getCafe(cafeID: String): DataSnapshot? {
        return try {
            cafesRef.child(cafeID).get().await()
        }catch (e: Exception) {
            null
        }
    }

    fun sendUserInfoToDatabase(email: String, fullName: String, password: String) {
        getCurrentUserID()?.let {
            usersRef.child(it).setValue(User(it, email, fullName, password)) }
            auth.currentUser?.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(fullName).build())
    }

    fun getDisplayName(): String?{
        return auth.currentUser?.displayName
    }
}