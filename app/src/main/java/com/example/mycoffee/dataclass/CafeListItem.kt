package com.example.mycoffee.dataclass

data class CafeListItem(
    var cafe: Cafe ?= null,
    var reward: Reward ?= null)
