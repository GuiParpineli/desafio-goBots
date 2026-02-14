package com.gobots.dataprovider.document

import com.gobots.model.DeliveryStatus
import com.gobots.model.EventStatus
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

data class OrderPayloadEmbedded(
    val id: String,
    val event: EventStatus,
    val orderID: String,
    val storeID: String,
    val timestamp: Long,
)

@Document(collection = "webhook_deliveries")
@CompoundIndex(name = "status_next_attempt", def = "{'status': 1, 'nextAttemptAt': 1}")
data class OrderDeliveryDocument(
    @Id val id: String? = null,
    @Indexed val subscriptionId: String,
    val callbackUrl: String,
    @Indexed val eventID: String,
    val event: EventStatus,
    val orderID: String,
    @Indexed val storeID: String,
    val payload: OrderPayloadEmbedded,
    @Indexed val status: DeliveryStatus,
    val attempts: Int,
    val nextAttemptAt: Long,
    val lastError: String?,
    val createdAt: Long,
)