package com.gobots.receiverapi.core.data

import com.gobots.receiverapi.core.model.OrderEvent
import com.gobots.receiverapi.core.model.OrderSnapshot

interface OrderClient {
    fun getOrderSnapshot(orderID: String): OrderSnapshot?
}
