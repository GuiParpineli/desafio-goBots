package com.gobots.receiverapi.adapter.mapper

import com.gobots.receiverapi.adapter.infra.dataprovider.client.MarketplaceOrderResponse
import com.gobots.receiverapi.adapter.infra.dataprovider.repository.entity.OrderEventDocument
import com.gobots.receiverapi.adapter.infra.dataprovider.repository.entity.OrderSnapshotEmbedded
import com.gobots.receiverapi.core.model.OrderEvent
import com.gobots.receiverapi.core.model.OrderSnapshot
import com.gobots.receiverapi.entrypoint.controller.dto.OrderEventResponseDTO
import com.gobots.receiverapi.entrypoint.controller.dto.OrderReceiveDTO
import org.springframework.data.mongodb.core.aggregation.MergeOperation.UniqueMergeId.id

object Mapper {

    fun OrderEvent.toDocument() = OrderEventDocument(
        id = this.id,
        eventId = this.eventID,
        event = this.event,
        orderId = this.orderID,
        storeId = this.storeID,
        timestamp = this.timestamp,
        orderSnapshot = this.orderSnapshot?.let {
            OrderSnapshotEmbedded(
                orderId = it.orderId,
                storeId = it.storeId,
                status = it.status,
                productsIDs = it.productsIDs,
                clientID = it.clientID,
                priority = it.priority,
                createdAt = it.createdAt,
            )
        },
        processed = this.processed,
    )

    fun OrderEventDocument.toDomain() = OrderEvent(
        id = this.id!!,
        eventID = this.eventId,
        event = this.event,
        orderID = this.orderId,
        storeID = this.storeId,
        timestamp = this.timestamp,
        orderSnapshot = this.orderSnapshot?.let {
            OrderSnapshot(
                orderId = it.orderId,
                storeId = it.storeId,
                status = it.status,
                productsIDs = it.productsIDs,
                clientID = it.clientID,
                priority = it.priority,
                createdAt = it.createdAt,
            )
        },
        processed = this.processed,
    )

    fun OrderReceiveDTO.toDomain() = OrderEvent(
        eventID = this.eventID,
        event = this.event,
        orderID = this.orderID,
        storeID = this.storeID,
        timestamp = this.timestamp,
    )

    fun OrderEvent.toDTO() = OrderEventResponseDTO(
        id = this.id!!,
        event = this.event,
        orderID = this.orderID,
        storeID = this.storeID,
        timestamp = this.timestamp,
    )

    fun MarketplaceOrderResponse.toSnapShot() = OrderSnapshot(
        orderId = id,
        storeId = storeID,
        productsIDs = productsIDs,
        clientID = clientID,
        priority = 0,
        status = status,
        createdAt = createdAt,
    )
}