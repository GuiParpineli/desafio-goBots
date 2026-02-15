package com.gobots.usecase.delivery

import com.gobots.model.EventStatus
import com.gobots.model.OrderDelivery
import com.gobots.model.OrderPayload
import com.gobots.repository.OrderDeliveryRepository
import com.gobots.repository.OrderSubscriptionRepository

class OrderDeliveryUseCaseImpl(
    val deliveryRepo: OrderDeliveryRepository,
    val subscriptionRepo: OrderSubscriptionRepository
) : OrderDeliveryUseCase {
    override fun enqueue(eventStatus: EventStatus, orderId: String, storeId: String) {
        val subs = subscriptionRepo.findByStoreIdAndEvent(storeId)
        subs.forEach { sub ->
            val payload = OrderPayload(
                event = eventStatus,
                orderID = orderId,
                storeID = storeId
            )
            deliveryRepo.save(
                OrderDelivery(
                    subscriptionId = sub.id ?: error("subscription id missing"),
                    callbackUrl = sub.callbackUrl,
                    eventID = payload.eventID,
                    event = payload.event,
                    orderID = payload.orderID,
                    storeID = payload.storeID,
                    payload = payload,
                )
            )
        }
    }
}