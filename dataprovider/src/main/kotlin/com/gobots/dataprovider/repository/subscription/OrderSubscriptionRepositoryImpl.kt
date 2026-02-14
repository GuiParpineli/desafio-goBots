package com.gobots.dataprovider.repository.subscription

import com.gobots.dataprovider.mapper.Mapper.toDomain
import com.gobots.dataprovider.mapper.Mapper.toDocument
import com.gobots.model.EventStatus
import com.gobots.model.OrderSubscription
import com.gobots.repository.OrderSubscriptionRepository
import org.springframework.stereotype.Repository

@Repository
class OrderSubscriptionRepositoryImpl(
    private val mongo: OrderSubscriptionMongoRepository,
) : OrderSubscriptionRepository {

    override fun save(subscription: OrderSubscription): OrderSubscription =
        mongo.save(subscription.toDocument()).toDomain()

    override fun findByStoreIdAndEvent(storeId: String): List<OrderSubscription> =
        mongo.findByStoreIDsContainingAndActiveIsTrue(storeId)
            .map { it.toDomain() }

    override fun findAll(): List<OrderSubscription> =
        mongo.findAll().map { it.toDomain() }
}