package com.leprpht.heliosbackend

import com.leprpht.heliosbackend.controller.CollectibleController
import com.leprpht.heliosbackend.dto.UnpackedUser
import com.leprpht.heliosbackend.service.HeliosService
import com.leprpht.heliosbackend.util.TokenUtil
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockkObject
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import kotlin.test.assertEquals
import kotlin.test.assertContentEquals

@ExtendWith(MockKExtension::class)
class CollectibleControllerTest {

    @MockK
    lateinit var service: HeliosService

    private lateinit var controller: CollectibleController

    @BeforeEach
    fun setup() {
        controller = CollectibleController(service)
        mockkObject(TokenUtil)
    }

    @Test
    fun `getUserData returns user data`() {
        val username = "username"
        val header = "header"
        val token = "token"
        val collectibles = BooleanArray(5) { it % 2 == 0 }
        val response = UnpackedUser(username, collectibles)

        every { TokenUtil.extractToken(header) } returns token
        every { service.getUserData(token) } returns response

        val result: ResponseEntity<Any> = controller.getData(header)
        val resultBody = result.body as UnpackedUser

        assertEquals(HttpStatus.OK, result.statusCode)
        assertEquals(username, resultBody.username)
        assertContentEquals(collectibles, resultBody.collectibles)

        verify { TokenUtil.extractToken(header); service.getUserData(token) }
    }

    @Test
    fun `updateCollectibles calls service`() {
        val header = "header"
        val token = "token"
        val newCollectibles = BooleanArray(5) { it % 2 != 0 }

        every { TokenUtil.extractToken(header) } returns token
        every { service.updateCollectibles(token, newCollectibles) } returns Unit

        val result = controller.updateCollectibles(header, newCollectibles)
        assertEquals(HttpStatus.OK, result.statusCode)

        verify { TokenUtil.extractToken(header); service.updateCollectibles(token, newCollectibles) }
    }
}
