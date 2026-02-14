package com.gobots.dataprovider.repository.order

import com.gobots.dataprovider.mapper.Mapper.toDocument
import com.gobots.dataprovider.mapper.Mapper.toOrder
import com.gobots.model.Order
import com.gobots.repository.OrderRepository
import org.springframework.stereotype.Repository

@Repository
class OrderRepositoryImpl(
    private val mongo: OrderMongoRepository,
) : OrderRepository {

    override fun save(order: Order): Order =
        mongo.save(order.toDocument()).toOrder()

    override fun findById(id: String): Order? =
        mongo.findById(id).map { it.toOrder() }.orElse(null)

    override fun findAll(): List<Order> =
        mongo.findAll().map { it.toOrder() }

    override fun findByStoreId(storeId: String): List<Order> =
        mongo.findByStoreId(storeId).map { it.toOrder() }
}