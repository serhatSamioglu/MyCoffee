package com.example.mycoffee.dataclass

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CafeListItem(
    var cafe: Cafe ? = null,
    var reward: Reward ? = null
) : Parcelable
