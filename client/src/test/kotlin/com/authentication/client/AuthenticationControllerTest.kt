package com.authentication.client

import com.authentication.client.adapters.inbound.AuthenticationController
import com.authentication.client.core.domain.model.Customer
import com.authentication.client.ports.inbound.AuthenticationUseCase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class AuthenticationControllerTest {

    private lateinit var useCase: AuthenticationUseCase
    private lateinit var controller: AuthenticationController

    @BeforeEach
    fun setUp() {
        useCase = mock(AuthenticationUseCase::class.java)
        controller = AuthenticationController(useCase)
    }

    @Test
    fun `authenticate should return token`() {
        // Arrange
        val username = "user"
        val password = "pass"
        val token = "generated-token"
        `when`(useCase.authenticate(username, password)).thenReturn(token)

        // Act
        val response: ResponseEntity<String> = controller.authenticate(username, password)

        // Assert
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(token, response.body)
        verify(useCase, times(1)).authenticate(username, password)
    }

    @Test
    fun `register should return created customer`() {
        // Arrange
        val customer = Customer("user", "pass", "123456789")
        `when`(useCase.register(customer)).thenReturn(customer)

        // Act
        val response: ResponseEntity<Customer> = controller.register(customer)

        // Assert
        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertEquals(customer, response.body)
        verify(useCase, times(1)).register(customer)
    }
}