package com.authentication.client

import com.authentication.client.adapters.inbound.PrivateController
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PrivateControllerTest {

    private val controller = PrivateController()

    @Test
    fun `message should return hello message`() {
        // Act
        val message = controller.message

        // Assert
        assertEquals("Hello from private API controller", message)
    }
}