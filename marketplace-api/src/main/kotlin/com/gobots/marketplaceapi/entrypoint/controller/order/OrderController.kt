package com.gobots.marketplaceapi.entrypoint.controller.order

import com.gobots.marketplaceapi.entrypoint.dto.CreateOrderRequest
import com.gobots.marketplaceapi.entrypoint.dto.OrderCompleteResponseDTO
import com.gobots.marketplaceapi.entrypoint.dto.OrderResponseDTO
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity

@Tag(name = "Pedidos")
interface OrderController {
    @Operation(
        summary = "Criar um pedido para storeId enviado com status order.created",
        description = """
            Cria um pedido com status order.created.
            Salva pedido no banco de dados.
            Cria evento para envio em webhooks cadastrado para storeId enviado. 
            Salva evento com status PENDING no banco de dados.
        """
    )
    fun create(req: CreateOrderRequest): ResponseEntity<OrderResponseDTO>

    @Operation(
        summary = "Atualiza status do pedido",
        description = """
            Pode ser enviado os status: order.created, order.paid, order.shipped,order.completed, order.cancelled.
        """
    )
    fun updateStatus(orderId: String, status: String): ResponseEntity<OrderResponseDTO>

    @Operation(summary = "Busca pedido por ID")
    fun findById(orderId: String): ResponseEntity<OrderCompleteResponseDTO>

    @Operation(summary = "Busca todos os pedidos salvos no banco de dados")
    fun findAll(): ResponseEntity<List<OrderCompleteResponseDTO>>

    @Operation(summary = "Busca todos os pedidos salvos no banco de dados por ID da loja")
    fun findByStore(storeId: String): ResponseEntity<List<OrderCompleteResponseDTO>>
}
