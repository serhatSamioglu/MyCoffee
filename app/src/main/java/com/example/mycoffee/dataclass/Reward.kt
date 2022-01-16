package com.example.mycoffee.dataclass

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Reward(
    var coffeeID: String ?= null,
    var starCount: Int ?= null,
    var giftCount: Int ?= null): Parcelable
