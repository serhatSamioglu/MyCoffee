package com.example.mycoffee.cafelist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mycoffee.dataclass.Cafe
import com.example.mycoffee.dataclass.CafeListItem
import com.example.mycoffee.dataclass.Reward
import com.example.mycoffee.services.Firebase
import com.google.firebase.database.DataSnapshot
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList

class CafeListViewModel: ViewModel() {

    private val _cafeList = MutableStateFlow<ArrayList<CafeListItem>>(arrayListOf())
    val cafeList = _cafeList.asStateFlow() // stateflow sürümden dolayı sorun olabilir

    fun getCafes() {
        GlobalScope.launch {
            // _cafeList.emit(mapCafeList(Firebase.getCafes()))
            _cafeList.value = getCafeList(Firebase.getStars())
        }
    }

    private suspend fun getCafeList (dataSnapshot: DataSnapshot?): ArrayList<CafeListItem> {
        var tempCafeListItem: ArrayList<CafeListItem> = arrayListOf()

        dataSnapshot?.let { snapshot ->
            for (starSnapshot in snapshot.children){
                starSnapshot.getValue(Reward::class.java)?.let {
                    tempCafeListItem.add(CafeListItem(null, it)) // todo: sıralama algoritması eklenmesi lazım
                }
            }
            for ((index, cafeListItem) in tempCafeListItem.withIndex()) { // todo: returnu bu for bitince yapsa daha iyi olur
                cafeListItem.reward?.coffeeID?.let { coffeeID ->
                    Firebase.getCafe(coffeeID)?.let {  coffeeSnapshot ->
                        coffeeSnapshot.getValue(Cafe::class.java)?.let { cafeObject ->
                            tempCafeListItem[index].cafe = cafeObject
                        }
                    }
                }
            }
        }
        return tempCafeListItem // TODO iki listeyi bir obje yapabilriiz veya tek liste iki objeyi tutar
    }

    fun getDisplayName(): String? {
        return Firebase.getDisplayName()
    }
}