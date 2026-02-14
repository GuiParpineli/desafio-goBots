package com.gobots.marketplaceapi.entrypoint.dto

import jakarta.validation.constraints.NotBlank

data class CreateOrderRequest(
    @field:NotBlank val storeId: String,
)

data class OrderResponseDTO(
    val id: String,
    val storeId: String,
    val status: String,
    val createdAt: Long,
)