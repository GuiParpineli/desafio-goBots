package com.gobots.receiverapi.adapter.infra.dataprovider.repository.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

data class OrderSnapshotEmbedded(
    val orderId: String,
    val storeId: String,
    val productsIDs: List<String>,
    val clientID: String,
    val priority: Int,
    val status: String,
    val createdAt: Long,
)

@Document(collection = "received_events")
data class OrderEventDocument(
    @Id val id: String? = null,
    @Indexed(unique = true) val eventId: String,
    val event: String,
    val orderId: String,
    val storeId: String,
    val timestamp: Long,
    val orderSnapshot: OrderSnapshotEmbedded? = null,
    val receivedAt: Long = Instant.now().toEpochMilli(),
    val processed: Boolean = false,
)