package com.example.mycoffee.dataclass

data class User(
    var uid: String ?= null,
    var email: String ?= null,
    var fullName: String ?= null,
    var password: String ?= null)
