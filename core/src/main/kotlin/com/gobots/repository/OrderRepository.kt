package com.gobots.repository

import com.gobots.model.Order

interface OrderRepository {
    fun save(order: Order): Order
    fun findById(id: String): Order?
    fun findAll(): List<Order>
    fun findByStoreId(storeId: String): List<Order>
}