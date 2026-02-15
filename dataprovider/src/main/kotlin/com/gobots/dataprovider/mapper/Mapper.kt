package com.gobots.dataprovider.mapper

import com.gobots.dataprovider.document.*
import com.gobots.model.*

object Mapper {

    fun Order.toDocument() = OrderDocument(
        id = this.id,
        storeId = this.storeId,
        status = this.status,
        createdAt = this.createdAt,
    )

    fun OrderDocument.toOrder() = Order(
        id = this.id,
        storeId = this.storeId,
        status = this.status,
        createdAt = this.createdAt,
    )

    fun OrderSubscription.toDocument() = OrderSubscriptionDocument(
        id = this.id,
        storeIDs = this.storeIDs,
        callbackUrl = this.callbackUrl,
        active = this.active,
        createdAt = this.createdAt,
    )

    fun OrderSubscriptionDocument.toDomain() = OrderSubscription(
        id = this.id,
        storeIDs = this.storeIDs,
        callbackUrl = this.callbackUrl,
        active = this.active,
        createdAt = this.createdAt,
    )

    fun OrderDelivery.toDocument() = OrderDeliveryDocument(
        id = this.id,
        subscriptionId = this.subscriptionId,
        callbackUrl = this.callbackUrl,
        eventID = this.eventID,
        event = this.event,
        orderID = this.orderID,
        storeID = this.storeID,
        payload = OrderPayloadEmbedded(
            id = this.payload.eventID,
            event = this.payload.event,
            orderID = this.payload.orderID,
            storeID = this.payload.storeID,
            timestamp = this.payload.timestamp,
        ),
        status = this.status,
        attempts = this.attempts,
        nextAttemptAt = this.nextAttemptAt,
        lastError = this.lastError,
        createdAt = this.createdAt,
    )

    fun OrderDeliveryDocument.toDomain() = OrderDelivery(
        id = this.id,
        subscriptionId = this.subscriptionId,
        callbackUrl = this.callbackUrl,
        eventID = this.eventID,
        event = this.event,
        orderID = this.orderID,
        storeID = this.storeID,
        payload = OrderPayload(
            eventID = this.payload.id,
            event = this.payload.event,
            orderID = this.payload.orderID,
            storeID = this.payload.storeID,
            timestamp = this.payload.timestamp,
        ),
        status = this.status,
        attempts = this.attempts,
        nextAttemptAt = this.nextAttemptAt,
        lastError = this.lastError,
        createdAt = this.createdAt,
    )
}