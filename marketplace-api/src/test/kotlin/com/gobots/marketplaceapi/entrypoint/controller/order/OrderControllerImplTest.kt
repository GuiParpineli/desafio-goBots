package com.gobots.marketplaceapi.entrypoint.controller.order

import com.fasterxml.jackson.databind.ObjectMapper
import com.gobots.exception.NotFoundException
import com.gobots.marketplaceapi.adapter.configuration.exception.GlobalExceptionHandler
import com.gobots.marketplaceapi.entrypoint.dto.CreateOrderRequest
import com.gobots.marketplaceapi.entrypoint.dto.OrderCompleteResponseDTO
import com.gobots.marketplaceapi.entrypoint.dto.OrderResponseDTO
import com.gobots.marketplaceapi.service.OrderService
import com.gobots.model.EventStatus
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders

class OrderControllerImplTest {

    private lateinit var mockMvc: MockMvc
    private val orderService: OrderService = mockk()
    private val objectMapper = ObjectMapper()

    @BeforeEach
    fun setup() {
        val controller = OrderControllerImpl(orderService)
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
            .setControllerAdvice(GlobalExceptionHandler())
            .build()
    }

    @Nested
    @DisplayName("POST /orders")
    inner class CreateOrder {

        @Test
        @DisplayName("Deve retornar 201 quando criar um novo pedido com sucesso")
        fun shouldReturn201WhenOrderCreated() {
            val request = CreateOrderRequest(storeId = "store-123")
            val response = OrderResponseDTO(id = "order-123", status = "order.created")
            every { orderService.create(request.storeId) } returns response

            mockMvc.perform(
                post("/orders")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
                .andExpect(status().isCreated)
                .andExpect(jsonPath("$.id").value("order-123"))
                .andExpect(jsonPath("$.status").value("order.created"))

            verify(exactly = 1) { orderService.create(request.storeId) }
        }

        @Test
        @DisplayName("Deve retornar 400 quando storeId estiver em branco")
        fun shouldReturn400WhenStoreIdIsEmpty() {
            val request = CreateOrderRequest(storeId = "")

            mockMvc.perform(
                post("/orders")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors[0].field").value("storeId"))
        }
    }

    @Nested
    @DisplayName("PATCH /orders/{orderId}/{status}")
    inner class UpdateStatus {

        @Test
        @DisplayName("Deve retornar 202 quando o status do pedido for atualizado com sucesso")
        fun shouldReturn202WhenStatusUpdated() {
            val orderId = "order-123"
            val statusStr = "order.paid"
            val response = OrderResponseDTO(id = orderId, status = statusStr)
            every { orderService.updateEvent(orderId, EventStatus.PAID) } returns response

            mockMvc.perform(
                patch("/orders/{orderId}/{status}", orderId, statusStr)
            )
                .andExpect(status().isAccepted)
                .andExpect(jsonPath("$.id").value(orderId))
                .andExpect(jsonPath("$.status").value(statusStr))

            verify(exactly = 1) { orderService.updateEvent(orderId, EventStatus.PAID) }
        }

        @Test
        @DisplayName("Deve retornar 400 quando o eventStatus for inválido")
        fun shouldReturn400WhenStatusIsInvalid() {
            val orderId = "order-123"
            val invalidStatus = "invalid.status"

            mockMvc.perform(
                patch("/orders/{orderId}/{status}", orderId, invalidStatus)
            )
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.message").value("Unsupported event: invalid.status"))
        }

        @Test
        @DisplayName("Deve retornar 404 quando o orderId não for encontrado")
        fun shouldReturn404WhenOrderIdNotFound() {
            val orderId = "not-found"
            val statusStr = "order.paid"
            every { orderService.updateEvent(orderId, any()) } throws NotFoundException()

            mockMvc.perform(
                patch("/orders/{orderId}/{status}", orderId, statusStr)
            )
                .andExpect(status().isNotFound)
        }
    }

    @Nested
    @DisplayName("GET /orders/id/{orderId}")
    inner class FindById {

        @Test
        @DisplayName("Deve retornar 200 quando o pedido for encontrado por ID")
        fun shouldReturn200WhenOrderFound() {
            val orderId = "order-123"
            val response = createOrderCompleteResponseDTO(id = orderId)
            every { orderService.findById(orderId) } returns response

            mockMvc.perform(
                get("/orders/id/{orderId}", orderId)
            )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.id").value(orderId))

            verify(exactly = 1) { orderService.findById(orderId) }
        }

        @Test
        @DisplayName("Deve retornar 404 quando o pedido não for encontrado por ID")
        fun shouldReturn404WhenOrderNotFound() {
            val orderId = "not-found"
            every { orderService.findById(orderId) } throws NotFoundException()

            mockMvc.perform(
                get("/orders/id/{orderId}", orderId)
            )
                .andExpect(status().isNotFound)
        }
    }

    @Nested
    @DisplayName("GET /orders")
    inner class FindAll {

        @Test
        @DisplayName("Deve retornar lista de eventos e status 200")
        fun shouldReturn200AndListOfOrders() {
            val orders = listOf(createOrderCompleteResponseDTO())
            every { orderService.findAll() } returns orders

            mockMvc.perform(
                get("/orders")
            )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.length()").value(1))

            verify(exactly = 1) { orderService.findAll() }
        }

        @Test
        @DisplayName("Deve retornar 404 quando o banco estiver vazio")
        fun shouldReturn404WhenNoOrdersExist() {
            every { orderService.findAll() } throws NotFoundException()

            mockMvc.perform(
                get("/orders")
            )
                .andExpect(status().isNotFound)
        }
    }

    @Nested
    @DisplayName("GET /orders/store/{storeId}")
    inner class FindByStore {

        @Test
        @DisplayName("Deve retornar eventos da loja e status 200")
        fun shouldReturn200AndOrdersByStore() {
            val storeId = "store-123"
            val orders = listOf(createOrderCompleteResponseDTO(storeId = storeId))
            every { orderService.findByStoreId(storeId) } returns orders

            mockMvc.perform(
                get("/orders/store/{storeId}", storeId)
            )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].storeID").value(storeId))

            verify(exactly = 1) { orderService.findByStoreId(storeId) }
        }

        @Test
        @DisplayName("Deve retornar 404 quando storeId não encontrar eventos")
        fun shouldReturn404WhenNoOrdersForStore() {
            val storeId = "empty-store"
            every { orderService.findByStoreId(storeId) } throws NotFoundException()

            mockMvc.perform(
                get("/orders/store/{storeId}", storeId)
            )
                .andExpect(status().isNotFound)
        }
    }

    private fun createOrderCompleteResponseDTO(
        id: String = "order-123",
        storeId: String = "store-123",
        status: String = "order.created"
    ) = OrderCompleteResponseDTO(
        id = id,
        storeID = storeId,
        productsIDs = listOf("prod-1"),
        clientID = "client-1",
        priority = 1,
        status = status,
        createdAt = System.currentTimeMillis()
    )
}
