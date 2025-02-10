package com.authentication.client

import com.authentication.client.adapters.outbound.JwtServiceAdapter
import com.authentication.client.core.domain.model.Customer
import com.authentication.client.ports.outbound.CustomerRepositoryPort
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import java.util.*

class JwtServiceAdapterTest {

    private lateinit var encoder: JwtEncoder
    private lateinit var customerRepository: CustomerRepositoryPort
    private lateinit var passwordEncoder: PasswordEncoder
    private lateinit var jwtService: JwtServiceAdapter

    @BeforeEach
    fun setUp() {
        encoder = mock(JwtEncoder::class.java)
        customerRepository = mock(CustomerRepositoryPort::class.java)
        passwordEncoder = mock(PasswordEncoder::class.java)
        jwtService = JwtServiceAdapter(encoder, customerRepository, passwordEncoder)
    }

    @Test
    fun `generateToken should return token`() {
        // Arrange
        val username = "user"
        val password = "pass"
        val customer = Customer(username, "encodedPass", "123456789")
        val tokenValue = "fake-jwt-token"

        // Mock do JwtEncoder para retornar um Jwt válido
        val jwt = mock(Jwt::class.java)
        `when`(jwt.tokenValue).thenReturn(tokenValue)
        `when`(encoder.encode(any(JwtEncoderParameters::class.java))).thenReturn(jwt)

        // Mock do CustomerRepository e PasswordEncoder
        `when`(customerRepository.findByUsername(username)).thenReturn(Optional.of(customer))
        `when`(passwordEncoder.matches(password, customer.password)).thenReturn(true)

        // Act
        val token = jwtService.generateToken(username, password)

        // Assert
        assertEquals(tokenValue, token) // Verifica se o token retornado é o esperado
        verify(customerRepository, times(1)).findByUsername(username)
        verify(passwordEncoder, times(1)).matches(password, customer.password)
        verify(encoder, times(1)).encode(any(JwtEncoderParameters::class.java))
    }
}