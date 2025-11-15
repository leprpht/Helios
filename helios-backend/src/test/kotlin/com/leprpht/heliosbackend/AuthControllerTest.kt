package com.leprpht.heliosbackend

import com.leprpht.heliosbackend.controller.AuthController
import com.leprpht.heliosbackend.dto.LoginData
import com.leprpht.heliosbackend.dto.UpdatePasswordDto
import com.leprpht.heliosbackend.service.HeliosService
import io.mockk.every
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.justRun
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import kotlin.test.assertEquals

class AuthControllerTest {

    @MockK
    lateinit var heliosService: HeliosService

    @InjectMockKs
    lateinit var authController: AuthController

    @BeforeEach
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `register returns token when successful`() {
        val loginData = LoginData("user", "pass")
        every { heliosService.registerAndGetToken(loginData.username, loginData.password) } returns "token123"

        val response: ResponseEntity<Any> = authController.register(loginData)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(mapOf("token" to "token123"), response.body)
    }

    @Test
    fun `register returns 400 when user exists`() {
        val loginData = LoginData("user", "pass")
        every { heliosService.registerAndGetToken(loginData.username, loginData.password) } throws IllegalArgumentException("User already exists")

        val response: ResponseEntity<Any> = authController.register(loginData)

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        assertEquals("User already exists", response.body)
    }

    @Test
    fun `login returns token when credentials valid`() {
        val loginData = LoginData("user", "pass")
        every { heliosService.loginAndGetToken(loginData.username, loginData.password) } returns "token123"

        val response = authController.login(loginData)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(mapOf("token" to "token123"), response.body)
    }

    @Test
    fun `login returns 400 when credentials invalid`() {
        val loginData = LoginData("user", "pass")
        every { heliosService.loginAndGetToken(loginData.username, loginData.password) } throws IllegalArgumentException("Invalid username or password")

        val response = authController.login(loginData)

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        assertEquals("Invalid username or password", response.body)
    }

    @Test
    fun `update password returns new token`() {
        val tokenHeader = "Bearer token"
        val cleanToken = "token"
        val passwordUpdate = UpdatePasswordDto("oldPass", "newPass")

        every { heliosService.updatePasswordAndGetToken(cleanToken, passwordUpdate.oldPassword, passwordUpdate.newPassword) } returns "newToken"

        val response = authController.updatePassword(tokenHeader, passwordUpdate)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(mapOf("token" to "newToken"), response.body)
    }

    @Test
    fun `update password returns 400 when exception thrown`() {
        val tokenHeader = "Bearer token"
        val cleanToken = "token"
        val passwordUpdate = UpdatePasswordDto("oldPass", "newPass")

        every { heliosService.updatePasswordAndGetToken(cleanToken, passwordUpdate.oldPassword, passwordUpdate.newPassword) } throws IllegalArgumentException("Old password incorrect")

        val response = authController.updatePassword(tokenHeader, passwordUpdate)

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        assertEquals("Old password incorrect", response.body)
    }

    @Test
    fun `delete account returns ok when successful`() {
        val tokenHeader = "Bearer token"
        val cleanToken = "token"
        val loginData = LoginData("user", "pass")

        justRun { heliosService.deleteUser(cleanToken, loginData.username, loginData.password) }

        val response = authController.deleteAccount(tokenHeader, loginData)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals("Account deleted successfully", response.body)
    }

    @Test
    fun `delete account returns 400 when service throws`() {
        val tokenHeader = "Bearer token"
        val cleanToken = "token"
        val loginData = LoginData("user", "pass")

        every { heliosService.deleteUser(cleanToken, loginData.username, loginData.password) } throws IllegalArgumentException("Credentials mismatch")

        val response = authController.deleteAccount(tokenHeader, loginData)

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        assertEquals("Credentials mismatch", response.body)
    }
}
