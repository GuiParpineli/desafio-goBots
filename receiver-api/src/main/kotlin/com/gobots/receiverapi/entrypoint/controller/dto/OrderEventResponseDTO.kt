package com.gobots.receiverapi.entrypoint.controller.dto

class OrderEventResponseDTO(
    val id: String,
    val event: String,
    val orderID: String,
    val storeID: String,
    val timestamp: Long
)