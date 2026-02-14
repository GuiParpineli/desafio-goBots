package com.gobots.model

import java.time.Instant
import java.util.*

data class OrderPayload(
    val id: String = "event-${UUID.randomUUID()}",
    val event: EventStatus,
    val orderID: String,
    val storeID: String,
    val timestamp: Long = Instant.now().toEpochMilli()
)

data class OrderDelivery(
    val id: String? = null,
    val subscriptionId: String,
    val callbackUrl: String,
    val eventID: String,
    val event: EventStatus,
    val orderID: String,
    val storeID: String,
    val payload: OrderPayload,
    val status: DeliveryStatus = DeliveryStatus.PENDING,
    val attempts: Int = 0,
    val nextAttemptAt: Long = Instant.now().toEpochMilli(),
    val lastError: String? = null,
    val createdAt: Long = Instant.now().toEpochMilli(),
)
