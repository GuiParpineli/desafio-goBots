package com.gobots.receiverapi.adapter.infra.repository.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document(collection = "received_events")
data class OrderEventDocument(
    @Id val id: String? = null,
    @Indexed(unique = true) val eventId: String,
    val event: String,
    val orderId: String,
    val storeId: String,
    val timestamp: Long,
    val receivedAt: Long = Instant.now().toEpochMilli(),
    val processed: Boolean = false,
)
