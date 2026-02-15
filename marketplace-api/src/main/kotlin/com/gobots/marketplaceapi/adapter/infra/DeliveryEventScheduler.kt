package com.gobots.marketplaceapi.adapter.infra

import com.gobots.model.DeliveryStatus
import com.gobots.model.OrderDelivery
import com.gobots.repository.OrderDeliveryRepository
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import java.time.Instant

@Component
class DeliveryEventScheduler(
    private val deliveryRepo: OrderDeliveryRepository,
) {
    private val log = LoggerFactory.getLogger(DeliveryEventScheduler::class.java)
    private val restClient = RestClient.create()

    companion object {
        private const val BATCH_SIZE = 50
        private const val MAX_ATTEMPTS = 5
        private val BACKOFF_MILLIS = longArrayOf(
            60_000,          // 1 min
            5 * 60_000,      // 5 min
            15 * 60_000,     // 15 min
            60 * 60_000,     // 1 h
            6 * 60 * 60_000, // 6 h
        )
    }

    @Scheduled(fixedDelay = 5_000)
    fun processPending() {
        val now = Instant.now().toEpochMilli()
        val due = deliveryRepo.findDue(
            limit = BATCH_SIZE,
            timeStamp = now,
            status = setOf(DeliveryStatus.PENDING, DeliveryStatus.RETRY),
        )
        due.forEach { deliver(it) }
    }

    private fun deliver(d: OrderDelivery) {
        val result = runCatching {
            restClient.post()
                .uri(d.callbackUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .body(d.payload)
                .retrieve()
                .toBodilessEntity()
        }

        if (result.isSuccess) {
            deliveryRepo.save(d.copy(status = DeliveryStatus.DELIVERED, lastError = null))
            log.info("Delivered {} to {}", d.eventID, d.callbackUrl)
            return
        }

        val attempt = d.attempts + 1
        val error = result.exceptionOrNull()?.message

        if (attempt >= MAX_ATTEMPTS) {
            deliveryRepo.save(d.copy(status = DeliveryStatus.DEAD, attempts = attempt, lastError = error))
            log.warn("Dead-lettered {} after {} attempts: {}", d.eventID, attempt, error)
            return
        }

        val nextAt = Instant.now().toEpochMilli() + BACKOFF_MILLIS[attempt - 1]
        deliveryRepo.save(
            d.copy(
                status = DeliveryStatus.RETRY,
                attempts = attempt,
                nextAttemptAt = nextAt,
                lastError = error,
            )
        )
        log.warn("Retry #{} for {} scheduled at {}", attempt, d.eventID, Instant.ofEpochMilli(nextAt))
    }
}