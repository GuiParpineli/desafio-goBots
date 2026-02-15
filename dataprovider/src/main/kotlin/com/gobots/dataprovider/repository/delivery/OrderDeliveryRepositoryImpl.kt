package com.gobots.dataprovider.repository.delivery

import com.gobots.dataprovider.mapper.Mapper.toDomain
import com.gobots.dataprovider.mapper.Mapper.toDocument
import com.gobots.model.DeliveryStatus
import com.gobots.model.OrderDelivery
import com.gobots.repository.OrderDeliveryRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Repository

@Repository
class OrderDeliveryRepositoryImpl(
    private val mongo: OrderDeliveryMongoRepository,
) : OrderDeliveryRepository {

    override fun save(delivery: OrderDelivery): OrderDelivery =
        mongo.save(delivery.toDocument()).toDomain()

    override fun getPendingDeliveries(limit: Int, timeStamp: Long, status: Set<DeliveryStatus>): List<OrderDelivery> =
        mongo.findByStatusInAndNextAttemptAtLessThanEqual(
            status = status.map { it.name }.toSet(),
            nextAttemptAt = timeStamp,
            pageable = PageRequest.of(0, limit),
        ).map { it.toDomain() }
}