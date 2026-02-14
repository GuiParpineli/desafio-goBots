package com.gobots.receiverapi.adapter.mapper

import com.gobots.receiverapi.adapter.infra.repository.entity.OrderEventDocument
import com.gobots.receiverapi.core.model.OrderEvent
import com.gobots.receiverapi.entrypoint.controller.dto.OrderEventResponseDTO
import com.gobots.receiverapi.entrypoint.controller.dto.OrderReceiveDTO

object Mapper {
    fun OrderEvent.toDocument(): OrderEventDocument {
        return OrderEventDocument(
            eventId = this.id,
            event = this.event,
            orderId = this.orderID,
            storeId = this.storeID,
            timestamp = this.timestamp
        )
    }

    fun OrderEventDocument.toDomain() = OrderEvent(
        id = this.eventId,
        event = this.event,
        orderID = this.orderId,
        storeID = this.storeId,
        timestamp = this.timestamp
    )

    fun OrderReceiveDTO.toDomain() = OrderEvent(
        id = this.id,
        event = this.event,
        orderID = this.orderID,
        storeID = this.storeID,
        timestamp = this.timestamp
    )

    fun OrderEvent.toDTO() = OrderEventResponseDTO(
        id = this.id,
        event = this.event,
        orderID = this.orderID,
        storeID = this.storeID,
        timestamp = this.timestamp
    )
}