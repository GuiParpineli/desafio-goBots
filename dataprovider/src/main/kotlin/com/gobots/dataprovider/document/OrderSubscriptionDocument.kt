package com.gobots.dataprovider.document

import com.gobots.model.EventStatus
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "webhook_subscriptions")
@CompoundIndex(name = "store_event_active", def = "{'storeId': 1, 'active': 1}")
data class OrderSubscriptionDocument(
    @Id val id: String? = null,
    @Indexed val storeIDs: List<String>,
    val callbackUrl: String,
    @Indexed val active: Boolean = true,
    val createdAt: Long,
)