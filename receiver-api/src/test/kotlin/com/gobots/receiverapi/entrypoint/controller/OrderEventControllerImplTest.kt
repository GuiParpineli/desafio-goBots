package com.gobots.receiverapi.entrypoint.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.gobots.receiverapi.adapter.exception.GlobalExceptionHandler
import com.gobots.receiverapi.adapter.service.OrderEventService
import com.gobots.receiverapi.entrypoint.controller.dto.OrderEventResponseDTO
import com.gobots.receiverapi.entrypoint.controller.dto.OrderReceiveDTO
import io.mockk.*

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders

class OrderEventControllerImplTest {

    private lateinit var mockMvc: MockMvc
    private val service: OrderEventService = mockk()
    private val objectMapper = ObjectMapper()

    @BeforeEach
    fun setup() {
        val controller = OrderEventControllerImpl(service)
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
            .setControllerAdvice(GlobalExceptionHandler())
            .build()
    }

    @Test
    @DisplayName("Deve retornar 202 quando receber um evento novo com sucesso")
    fun shouldReturn202WhenReceiveNewEvent() {
        val requestBody = createOrderReceiveDTO()
        every { service.checkEventProcessed(requestBody.eventID) } returns false
        every { service.create(any()) } returns Unit

        mockMvc.perform(
            post("/order-receiver")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
        )
            .andExpect(status().isAccepted)

        verify(exactly = 1) { service.checkEventProcessed(requestBody.eventID) }
        verify(exactly = 1) { service.create(any()) }
    }

    @Test
    @DisplayName("Deve retornar 200 quando receber um evento duplicado")
    fun shouldReturn200WhenReceiveDuplicateEvent() {
        val requestBody = createOrderReceiveDTO()
        every { service.checkEventProcessed(requestBody.eventID) } returns true

        mockMvc.perform(
            post("/order-receiver")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
        )
            .andExpect(status().isOk)

        verify(exactly = 1) { service.checkEventProcessed(requestBody.eventID) }
        verify(exactly = 0) { service.create(any()) }
    }

    @Test
    @DisplayName("Deve retornar 400 quando o body do evento for inválido")
    fun shouldReturn400WhenReceiveInvalidBody() {
        val invalidRequestBody = createOrderReceiveDTO(eventID = "")

        mockMvc.perform(
            post("/order-receiver")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequestBody))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.message").value("Validation failed"))
            .andExpect(jsonPath("$.errors[0].field").value("eventID"))
    }

    @Test
    @DisplayName("Deve retornar lista de eventos e status 200")
    fun shouldReturnListOfEvents() {
        val events = listOf(
            OrderEventResponseDTO("1", "order.created", "8802123", "1", 1771086212L, true)
        )
        every { service.findAll() } returns events

        mockMvc.perform(get("/order-receiver/"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].id").value("1"))

        verify(exactly = 1) { service.findAll() }
    }

    @Test
    @DisplayName("Deve retornar eventos pelo ID e status 200")
    fun shouldReturnEventsById() {
        val id = "8802123"
        val events = listOf(
            OrderEventResponseDTO("1", "order.created", id, "1", 1771086212L, true)
        )
        every { service.findByID(id) } returns events

        mockMvc.perform(get("/order-receiver/byID/{id}", id))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].orderID").value(id))

        verify(exactly = 1) { service.findByID(id) }
    }

    private fun createOrderReceiveDTO(
        eventID: String = "213",
        event: String = "order.created",
        orderID: String = "8802123",
        storeID: String = "1",
        timestamp: Long = 1771086212L
    ) = OrderReceiveDTO(eventID, event, orderID, storeID, timestamp)
}
