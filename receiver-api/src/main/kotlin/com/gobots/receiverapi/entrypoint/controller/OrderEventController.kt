package com.gobots.receiverapi.entrypoint.controller

import com.gobots.receiverapi.entrypoint.controller.dto.OrderEventResponseDTO
import com.gobots.receiverapi.entrypoint.controller.dto.OrderReceiveDTO
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity

@Tag(name = "Pedidos")
interface OrderEventController {

    @Operation(
        summary = "WebHook pra receber um pedido. Enriquece e salva no banco de dados.",
        description = """
            Endpoint destinado para receber um pedido do Marketplace.
            Busca informações do pedido no Marketplace baseado no ID e salva no payload banco de dados.
            Marca item como processado.
            
            O campo id define um evento único, ao alterar o status será observado o campo orderID e o evento será um novo.
            Se o campo orderID não for encontrado, o evento será ignorado.
            O campo event pode ser preenchido com os valores: order.created, order.paid, order.shipped, order.cancelled.
            """
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Pedido recebido anteriormente, ignorado."),
            ApiResponse(
                responseCode = "202",
                description = "Pedido recebido com sucesso e enviado para processamento."
            ),
            ApiResponse(responseCode = "400", description = "Dados inválidos."),
            ApiResponse(responseCode = "500", description = "Erro interno do servidor")
        ]
    )
    fun receive(body: OrderReceiveDTO): ResponseEntity<Void>

    @Operation(summary = "Lista todos os pedidos salvos no banco de dados.")
    fun listAll(): List<OrderEventResponseDTO>

    @Operation(
        summary = "Lista pedido baseado no orderID",
        description = "Pode ser usado para verificar se o pedido foi recebido e se for processado."
    )
    fun listAll(id: String): List<OrderEventResponseDTO>
}
