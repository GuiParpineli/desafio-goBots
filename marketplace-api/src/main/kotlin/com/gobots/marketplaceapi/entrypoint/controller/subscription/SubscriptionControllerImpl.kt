package com.gobots.marketplaceapi.entrypoint.controller.subscription

import com.gobots.marketplaceapi.entrypoint.dto.CreateSubscriptionRequest
import com.gobots.marketplaceapi.entrypoint.dto.SubscriptionResponseDTO
import com.gobots.usecase.subscription.OrderSubscriptionUseCase
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/webhooks")
class SubscriptionControllerImpl(
    private val useCase: OrderSubscriptionUseCase,
) : SubscriptionController {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    override fun subscribe(@RequestBody @Valid req: CreateSubscriptionRequest): ResponseEntity<SubscriptionResponseDTO> {
        val sub = useCase.subscribe(req.storeIDs, req.callbackUrl)
        return ResponseEntity.ok(SubscriptionResponseDTO("webHook cadastrado com sucesso!"))
    }
}