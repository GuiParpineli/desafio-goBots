package com.gobots.marketplaceapi.entrypoint.controller.subscription

import com.gobots.marketplaceapi.entrypoint.dto.CreateSubscriptionRequest
import com.gobots.marketplaceapi.entrypoint.dto.SubscriptionResponseDTO
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity

@Tag(name = "Cadastro webHook")
interface SubscriptionController {
    @Operation(
        summary = "Cadastra um novo webHook",
        description = """
            Cadastra um novo webHook para receber eventos de pedidos.
            Caso a executar a API do receiver via Docker lembrar de colocar o nome do container no host.
            """
    )
    fun subscribe(req: CreateSubscriptionRequest): ResponseEntity<SubscriptionResponseDTO>
}
