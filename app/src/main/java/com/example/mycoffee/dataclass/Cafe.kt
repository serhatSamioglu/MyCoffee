package com.example.mycoffee.dataclass

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Cafe(
    var id: String ? = null,
    var name: String ? = null,
    var address: String ? = null,
    var hours: String ? = null,
    var requiredStar: Int ? = null,
    var logo: String ? = null
) : Parcelable
