package com.authentication.client.core.application.service

import com.authentication.client.adapters.outbound.CustomerRepositoryAdapter
import com.authentication.client.adapters.outbound.JwtServiceAdapter
import com.authentication.client.core.domain.model.Customer
import com.authentication.client.ports.inbound.AuthenticationUseCase
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.security.crypto.password.PasswordEncoder

class AuthenticationServiceTest {

    private lateinit var jwtService: JwtServiceAdapter
    private lateinit var customerRepository: CustomerRepositoryAdapter
    private lateinit var passwordEncoder: PasswordEncoder
    private lateinit var authenticationService: AuthenticationService

    @BeforeEach
    fun setUp() {
        jwtService = mock(JwtServiceAdapter::class.java)
        customerRepository = mock(CustomerRepositoryAdapter::class.java)
        passwordEncoder = mock(PasswordEncoder::class.java)
        authenticationService = AuthenticationService(jwtService, customerRepository, passwordEncoder)
    }

    @Test
    fun `authenticate should return token`() {
        // Arrange
        val username = "user"
        val password = "pass"
        val token = "generated-token"
        `when`(jwtService.generateToken(username, password)).thenReturn(token)

        // Act
        val result = authenticationService.authenticate(username, password)

        // Assert
        assertEquals(token, result)
        verify(jwtService, times(1)).generateToken(username, password)
    }

    @Test
    fun `register should save customer`() {
        // Arrange
        val customer = Customer("user", "pass", "123456789")
        `when`(customerRepository.existsByUsername(customer.username)).thenReturn(false)
        `when`(passwordEncoder.encode(customer.password)).thenReturn("encodedPass")
        `when`(customerRepository.save(any(Customer::class.java))).thenReturn(customer)

        // Act
        val result = authenticationService.register(customer)

        // Assert
        assertEquals(customer, result)
        verify(customerRepository, times(1)).existsByUsername(customer.username)
        verify(customerRepository, times(1)).save(any(Customer::class.java))
    }
}