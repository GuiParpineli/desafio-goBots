package com.gobots.marketplaceapi.entrypoint.dto

import jakarta.validation.constraints.NotBlank

data class CreateOrderRequest(
    @field:NotBlank val storeId: String,
)

data class OrderResponseDTO(
    val id: String,
    val status: String
)

data class OrderCompleteResponseDTO(
    val id: String,
    val storeID: String,
    val productsIDs: List<String>,
    val clientID: String,
    val priority: Int,
    val status: String,
    val createdAt: Long
)
