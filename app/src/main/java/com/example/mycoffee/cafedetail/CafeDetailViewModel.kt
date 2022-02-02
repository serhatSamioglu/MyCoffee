package com.example.mycoffee.cafedetail

import androidx.lifecycle.ViewModel
import com.example.mycoffee.dataclass.CafeListItem
import kotlinx.coroutines.flow.MutableStateFlow

class CafeDetailViewModel: ViewModel() {
    var cafeListItem = MutableStateFlow(CafeListItem(null, null))
}