package com.example.mycoffee.dataclass

data class ActivityObject(var type: String ?= null,
                          var date: String ?= null,
                          var cashierID: String ?= null,
                          var cafeID: String ?= null,
                          var customerID: String ?= null)
