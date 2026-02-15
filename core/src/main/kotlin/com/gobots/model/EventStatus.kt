package com.gobots.model

enum class EventStatus(private val allowedTransitions: () -> Set<EventStatus>) {
    CREATED({ setOf(PAID, CANCELLED) }),
    PAID({ setOf(SHIPPED, CANCELLED) }),
    SHIPPED({ setOf(COMPLETED, CANCELLED) }),
    COMPLETED({ emptySet() }),
    CANCELLED({ emptySet() });

    fun canTransitionTo(next: EventStatus): Boolean = next in allowedTransitions()

    fun transitionTo(next: EventStatus): EventStatus {
        require(canTransitionTo(next)) {
            "Invalid transition status: $this → $next (ALLOWED: ${allowedTransitions().map { it.wireName()}})"
        }
        return next
    }

    fun wireName(): String = when (this) {
        CREATED -> "order.created"
        PAID -> "order.paid"
        SHIPPED -> "order.shipped"
        COMPLETED -> "order.completed"
        CANCELLED -> "order.cancelled"
    }

    companion object {
        fun fromWireName(value: String): EventStatus =
            when (value) {
                "order.created" -> CREATED
                "order.paid" -> PAID
                "order.shipped" -> SHIPPED
                "order.completed" -> COMPLETED
                "order.cancelled" -> CANCELLED
                else -> throw IllegalArgumentException("Unsupported event: $value")
            }
    }
}