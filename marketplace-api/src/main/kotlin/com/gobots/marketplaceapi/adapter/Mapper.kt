package com.gobots.marketplaceapi.adapter

import com.gobots.marketplaceapi.entrypoint.dto.OrderCompleteResponseDTO
import com.gobots.marketplaceapi.entrypoint.dto.OrderResponseDTO
import com.gobots.model.Order
import com.gobots.model.OrderResponse

object Mapper {
    fun OrderResponse.toDTO() = OrderResponseDTO(this.id, this.status)
    fun Order.toDTO() = OrderCompleteResponseDTO(
        id = this.id!!,
        storeID = this.storeId,
        productsIDs = this.productsIDs,
        clientID = this.clientID,
        status = this.status.wireName(),
        priority = this.priority,
        createdAt = this.createdAt
    )
}