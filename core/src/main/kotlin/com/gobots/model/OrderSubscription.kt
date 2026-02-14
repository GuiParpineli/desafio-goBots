package com.gobots.model

import java.time.Instant

data class OrderSubscription(
    val id: String? = null,
    val storeIDs: List<String>,
    val callbackUrl: String,
    val active: Boolean = true,
    val createdAt: Long = Instant.now().toEpochMilli()
)