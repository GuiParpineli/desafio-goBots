package com.gobots.model

import java.time.Instant

data class Order(
    val id: String? = null,
    val storeId: String,
    val status: EventStatus = EventStatus.CREATED,
    val createdAt: Long = Instant.now().toEpochMilli(),
)

data class OrderResponse(val id: String)