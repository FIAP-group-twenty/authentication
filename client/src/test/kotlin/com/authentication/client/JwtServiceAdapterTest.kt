package com.authentication.client.adapters.outbound

import com.authentication.client.core.domain.model.Customer
import com.authentication.client.ports.outbound.CustomerRepositoryPort
import com.authentication.client.ports.outbound.TokenProviderPort
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import java.time.Instant
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
        `when`(customerRepository.findByUsername(username)).thenReturn(Optional.of(customer))
        `when`(passwordEncoder.matches(password, customer.password)).thenReturn(true)
        `when`(encoder.encode(any(JwtEncoderParameters::class.java))).thenReturn(mock())

        // Act
        val token = jwtService.generateToken(username, password)

        // Assert
        assertNotNull(token)
        verify(customerRepository, times(1)).findByUsername(username)
        verify(passwordEncoder, times(1)).matches(password, customer.password)
        verify(encoder, times(1)).encode(any(JwtEncoderParameters::class.java))
    }
}