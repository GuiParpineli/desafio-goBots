package com.gobots.model

import java.time.Instant

data class Order(
    val id: String? = null,
    val storeId: String,
    val productsIDs: List<String> = listOf("1", "2", "3"),
    val clientID: String = "1",
    val priority: Int = 0,
    val status: EventStatus = EventStatus.CREATED,
    val createdAt: Long = Instant.now().toEpochMilli()
)

data class OrderResponse(val id: String, val status: String)