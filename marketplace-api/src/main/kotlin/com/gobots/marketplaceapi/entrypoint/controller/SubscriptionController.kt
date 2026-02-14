package com.gobots.marketplaceapi.entrypoint.controller

import com.gobots.marketplaceapi.entrypoint.dto.CreateSubscriptionRequest
import com.gobots.marketplaceapi.entrypoint.dto.SubscriptionResponseDTO
import com.gobots.usecase.subscription.OrderSubscriptionUseCase
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/webhooks")
class SubscriptionController(
    private val useCase: OrderSubscriptionUseCase,
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun subscribe(@RequestBody @Valid req: CreateSubscriptionRequest): SubscriptionResponseDTO {
        val sub = useCase.subscribe(req.storeIDs, req.callbackUrl)
        return SubscriptionResponseDTO(
            id = sub.id!!,
            storeIDs = sub.storeIDs,
            callbackUrl = sub.callbackUrl
        )
    }
}
