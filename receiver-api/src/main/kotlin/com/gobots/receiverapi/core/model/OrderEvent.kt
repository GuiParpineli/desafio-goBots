package com.gobots.receiverapi.core.model

data class OrderEvent(
    val id: String,
    val event: String,
    val orderID: String,
    val storeID: String,
    val timestamp: Long
)
