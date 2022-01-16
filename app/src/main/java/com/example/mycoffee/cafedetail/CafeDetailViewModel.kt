package com.example.mycoffee.cafedetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mycoffee.dataclass.CafeListItem
import kotlinx.coroutines.flow.MutableStateFlow

class CafeDetailViewModel: ViewModel() {

    /*var cafeName = MutableStateFlow("")
    var starCount = MutableStateFlow("")
    var requiredStar = MutableStateFlow("")
    var giftCount = MutableStateFlow("")
    var cafeAddress = MutableStateFlow("")
    var cafeHours = MutableStateFlow("")*/
    var cafeListItem = MutableStateFlow(CafeListItem(null, null))

    // CafeListItem objesiini mutable adata olarak alıp kullanılabilir maplemektense
}