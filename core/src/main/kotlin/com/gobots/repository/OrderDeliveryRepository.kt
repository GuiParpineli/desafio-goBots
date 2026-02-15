package com.gobots.repository

import com.gobots.model.DeliveryStatus
import com.gobots.model.OrderDelivery

interface OrderDeliveryRepository {
    fun save(delivery: OrderDelivery): OrderDelivery
    fun getPendingDeliveries(limit: Int, timeStamp: Long, status: Set<DeliveryStatus>): List<OrderDelivery>
}