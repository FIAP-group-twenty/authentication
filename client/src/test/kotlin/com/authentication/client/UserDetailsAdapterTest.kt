package com.authentication.client

import com.authentication.client.adapters.outbound.UserDetailsAdapter
import com.authentication.client.core.domain.model.Customer
import com.authentication.client.ports.outbound.CustomerRepositoryPort
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.security.core.userdetails.UsernameNotFoundException
import java.util.*

class UserDetailsAdapterTest {

    private lateinit var customerRepository: CustomerRepositoryPort
    private lateinit var userDetailsAdapter: UserDetailsAdapter

    @BeforeEach
    fun setUp() {
        customerRepository = mock(CustomerRepositoryPort::class.java)
        userDetailsAdapter = UserDetailsAdapter(customerRepository)
    }

    @Test
    fun `loadUserByUsername should return UserDetails`() {
        // Arrange
        val username = "user"
        val customer = Customer(username, "pass", "123456789")
        `when`(customerRepository.findByUsername(username)).thenReturn(Optional.of(customer))

        // Act
        val userDetails = userDetailsAdapter.loadUserByUsername(username)

        // Assert
        assertEquals(username, userDetails.username)
        verify(customerRepository, times(1)).findByUsername(username)
    }

    @Test
    fun `loadUserByUsername should throw UsernameNotFoundException`() {
        // Arrange
        val username = "user"
        `when`(customerRepository.findByUsername(username)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows(UsernameNotFoundException::class.java) {
            userDetailsAdapter.loadUserByUsername(username)
        }
        verify(customerRepository, times(1)).findByUsername(username)
    }
}