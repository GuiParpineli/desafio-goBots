package com.gobots.receiverapi.core.model

data class OrderEvent(
    val id: String,
    val event: String,
    val orderID: String,
    val storeID: String,
    val timestamp: Long,
    val orderSnapshot: OrderSnapshot? = null,
    val processed: Boolean = false,
)

data class OrderSnapshot(
    val orderId: String,
    val storeId: String,
    val productsIDs: List<String>,
    val clientID: String,
    val priority: Int,
    val status: String,
    val createdAt: Long,
)