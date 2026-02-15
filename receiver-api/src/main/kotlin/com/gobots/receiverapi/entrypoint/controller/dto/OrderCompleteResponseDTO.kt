package com.gobots.receiverapi.entrypoint.controller.dto

import com.gobots.receiverapi.core.model.OrderSnapshot

data class OrderCompleteResponseDTO(
    val id: String,
    val event: String,
    val orderID: String,
    val storeID: String,
    val timestamp: Long,
    val orderSnapshot: OrderSnapshotDTO? = null,
    val processed: Boolean
)

data class OrderSnapshotDTO(
    val productsIDs: List<String>,
    val clientID: String,
    val priority: Int,
    val status: String,
    val createdAt: Long,
)
