package com.example.mycoffee.services

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.example.mycoffee.dataclass.ActivityObject
import com.example.mycoffee.dataclass.Reward
import com.example.mycoffee.dataclass.User
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.* // ktlint-disable no-wildcard-imports
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import com.google.firebase.auth.UserProfileChangeRequest

object Firebase { // FirebaseRepository

    private var auth: FirebaseAuth = FirebaseAuth.getInstance() // laneinit olmadı

    private var rewardsRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("Rewards")

    private var usersRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")

    private var cafesRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("Cafes")

    private var timestampRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("TIMESTAMP")

    private var activitiesRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("Activities")

    private var employeeRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("Employees")

    /*init {
        auth = FirebaseAuth.getInstance() // Todo: Firebase.auth' u önermedi
    }*/

    suspend fun createUser(activity: Activity, email: String, password: String): AuthResult? {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
        } catch (e: Exception) {
            Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
            null
        }
    }

    suspend fun signIn(activity: Activity, email: String, password: String): AuthResult? {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
        } catch (e: Exception) {
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
        return auth.currentUser?.uid // todo currentuser singleton icinde tutulabilir
    }

    suspend fun getStars(): DataSnapshot? {
        return try {
            getCurrentUserID()?.let { rewardsRef.child(it).get().await() }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getActivities(): DataSnapshot? {
        return try {
            getCafeID()?.let { activitiesRef.child("Cafes").child(it).get().await() }
        } catch (e: Exception) {
            null
        }
    }

    /*fun getStars2() {
        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d("evetnlistenertest", "onChildAdded:" + dataSnapshot.key!!)

                // A new comment has been added, add it to the displayed list
                // val comment = dataSnapshot.getValue<Comment>()

                // ...
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d("evetnlistenertest", "onChildChanged: ${dataSnapshot.key}")

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so displayed the changed comment.
                val commentKey = dataSnapshot.key

                // ...
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                Log.d("evetnlistenertest", "onChildRemoved:" + dataSnapshot.key!!)

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so remove it.
                val commentKey = dataSnapshot.key

                // ...
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d("evetnlistenertest", "onChildMoved:" + dataSnapshot.key!!)

                // A comment has changed position, use the key to determine if we are
                // displaying this comment and if so move it.
                val commentKey = dataSnapshot.key

                // ...
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("evetnlistenertest", "postComments:onCancelled", databaseError.toException())
            }
        }
        getCurrentUserID()?.let { starsRef.child(it).addChildEventListener(childEventListener) }
    }*/

    suspend fun getCafe(cafeID: String): DataSnapshot? {
        return try {
            cafesRef.child(cafeID).get().await()
        } catch (e: Exception) {
            null
        }
    }

    fun sendUserInfoToDatabase(email: String, fullName: String, password: String) {
        getCurrentUserID()?.let {
            usersRef.child(it).setValue(User(it, email, fullName, password, "customer")) }
            auth.currentUser?.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(fullName).build())
    }

    fun getDisplayName(): String? {
        return auth.currentUser?.displayName
    }

    suspend fun getRole(): String? {
        return try {
            getCurrentUserID()?.let { usersRef.child(it).child("role").get().await().value.toString() }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getCafeID(): String? {
        return try {
            getCurrentUserID()?.let { usersRef.child(it).child("cafeID").get().await().value.toString() }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getCashierCafeRequiredStar(cafeID: String): Int? {
        return try {
            cafesRef.child(cafeID).child("requiredStar").get().await().value.toString().toInt()
        } catch (e: Exception) {
            null
        }
    }

    fun giveStar(customerID: String, cafeID: String, requiredStar: Int, customerReward: Reward, cashierID: String?) {
        customerReward.starCount?.let { star_count ->
            if (star_count.inc() != requiredStar) { // hediye kahve kazanmıyorsa
                customerReward.apply {
                    starCount = star_count.inc()
                    rewardsRef.child(customerID).child(cafeID).setValue(customerReward)
                }
            } else {
                customerReward.apply {
                    starCount = 0
                    giftCount = customerReward.giftCount?.inc()
                    rewardsRef.child(customerID).child(cafeID).setValue(customerReward)
                }
            }
            sendActivityDatabase("star", customerID, cafeID, cashierID)
        }
    }

    fun useGift(customerID: String, cafeID: String, giftCount: Int?, cashierID: String?) {
        giftCount?.let { gift_count ->
            rewardsRef.child(customerID).child(cafeID).child("giftCount").setValue(gift_count.dec())
            sendActivityDatabase("gift", customerID, cafeID, cashierID)
        }
    }

    private fun sendActivityDatabase(type: String, customerID: String, cafeID: String, cashierID: String?) {
        cashierID?.let { cashier_ID ->
            timestampRef.setValue(ServerValue.TIMESTAMP).addOnSuccessListener {
                timestampRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        var date = dataSnapshot.value.toString()
                        activitiesRef.child("Cafes").child(cafeID).child(date).setValue(ActivityObject(type = type, date = date, cashierID = cashierID, customerID = customerID))
                        activitiesRef.child("Users").child(customerID).child(date).setValue(ActivityObject(type = type, date = date, cashierID = cashierID, cafeID = cafeID))
                    }
                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.w("SignedActivity.TAG", "LoginData:onCancelled", databaseError.toException())
                    }
                })
            }
        }
    }

    suspend fun getCustomerReward(customerID: String, cafeID: String): Reward? {
        return rewardsRef.child(customerID).child(cafeID).get().await().getValue(Reward::class.java)
            ?: Reward(cafeID, 0, 0)
    }

    suspend fun addEmployee(employeeID: String) {
        getCafeID()?.let { cafe_ID ->
            getFullName(employeeID)?.let { employee_FullName ->
                employeeRef.child(cafe_ID).child(employeeID).setValue(employee_FullName)
                usersRef.child(employeeID).child("cafeID").setValue(cafe_ID)
            }
        }
    }

    suspend fun removeEmployee(employeeID: String) {
        getCafeID()?.let { cafe_ID ->
            employeeRef.child(cafe_ID).child(employeeID).removeValue()
            usersRef.child(employeeID).child("cafeID").removeValue()
        }
    }

    suspend fun getFullName(userID: String): String? {
        return try {
            usersRef.child(userID).child("fullName").get().await().value.toString()
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getEmployees(): DataSnapshot? {
        return try {
            getCafeID()?.let { employeeRef.child(it).get().await() }
        } catch (e: Exception) {
            null
        }
    }
}