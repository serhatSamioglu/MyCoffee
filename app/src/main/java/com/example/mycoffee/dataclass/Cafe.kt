package com.example.mycoffee.dataclass

data class Cafe(
    var id: String ?= null,
    var name: String ?= null,
    var address: String ?= null,
    var hours: String ?= null,
    var requiredStar: Int ?= null)
