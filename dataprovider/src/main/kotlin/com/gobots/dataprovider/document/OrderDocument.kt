package com.gobots.dataprovider.document

import com.gobots.model.EventStatus
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "orders")
data class OrderDocument(
    @Id val id: String? = null,
    @Indexed val storeId: String,
    @Indexed val status: EventStatus,
    val createdAt: Long,
)
