package com.authentication.client.adapters.outbound

import com.authentication.client.core.domain.model.Customer
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import java.util.*

class CustomerRepositoryAdapterTest {

    private lateinit var jpaRepository: CustomerJpaRepository
    private lateinit var repositoryAdapter: CustomerRepositoryAdapter

    @BeforeEach
    fun setUp() {
        // Inicializa os mocks
        jpaRepository = mock(CustomerJpaRepository::class.java)
        repositoryAdapter = CustomerRepositoryAdapter(jpaRepository)
    }

    @Test
    fun `findByUsername should return customer`() {
        // Arrange
        val username = "user"
        val customer = Customer(username, "pass", "123456789")
        `when`(jpaRepository.findByUsername(username)).thenReturn(Optional.of(customer))

        // Act
        val result = repositoryAdapter.findByUsername(username)

        // Assert
        assertTrue(result.isPresent)
        assertEquals(customer, result.get())
        verify(jpaRepository, times(1)).findByUsername(username)
    }

    @Test
    fun `existsByUsername should return true if customer exists`() {
        // Arrange
        val username = "user"
        `when`(jpaRepository.existsByUsername(username)).thenReturn(true)

        // Act
        val result = repositoryAdapter.existsByUsername(username)

        // Assert
        assertTrue(result)
        verify(jpaRepository, times(1)).existsByUsername(username)
    }

    @Test
    fun `save should return saved customer`() {
        // Arrange
        val customer = Customer("user", "pass", "123456789")
        `when`(jpaRepository.save(customer)).thenReturn(customer)

        // Act
        val result = repositoryAdapter.save(customer)

        // Assert
        assertEquals(customer, result)
        verify(jpaRepository, times(1)).save(customer)
    }
}