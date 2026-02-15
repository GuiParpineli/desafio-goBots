package com.gobots.receiverapi.adapter.service

import com.gobots.receiverapi.core.data.OrderClient
import com.gobots.receiverapi.core.data.repository.OrderEventRepository
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class ReceiverEventScheduler(
    private val orderRepo: OrderEventRepository,
    private val client: OrderClient
) {
    private val log = LoggerFactory.getLogger(ReceiverEventScheduler::class.java)

    @Scheduled(fixedDelay = 10_000)
    fun processPending() {
        val pendingEvents = orderRepo.findPendingEvents()
        pendingEvents.forEach { event ->
            log.info("Processing event: ${event.id}")
            client.getOrderSnapshot(event.orderID)?.let {
                orderRepo.save(
                    event.copy(processed = true, orderSnapshot = it)
                )
            }
        }
    }
}