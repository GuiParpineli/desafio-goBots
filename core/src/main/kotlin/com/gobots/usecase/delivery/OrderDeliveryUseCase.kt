package com.gobots.usecase.delivery

import com.gobots.model.EventStatus

interface OrderDeliveryUseCase {
    fun enqueue(eventStatus: EventStatus, orderId: String, storeId: String)
}