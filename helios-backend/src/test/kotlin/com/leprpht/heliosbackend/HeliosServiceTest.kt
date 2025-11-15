package com.leprpht.heliosbackend

import com.leprpht.heliosbackend.constants.COLLECTIBLES_COUNT
import com.leprpht.heliosbackend.model.User
import com.leprpht.heliosbackend.repository.HeliosRepository
import com.leprpht.heliosbackend.service.HeliosService
import com.leprpht.heliosbackend.util.Base64Parser
import com.leprpht.heliosbackend.util.JwtUtil
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.security.crypto.password.PasswordEncoder

@ExtendWith(MockKExtension::class)
class HeliosServiceTest {

    @MockK
    lateinit var repo: HeliosRepository

    @MockK
    lateinit var passwordEncoder: PasswordEncoder

    @InjectMockKs
    lateinit var service: HeliosService

    @BeforeEach
    fun setup() {
        mockkObject(JwtUtil)
        mockkObject(Base64Parser)
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `registerAndGetToken throws when user exists`() {
        every { repo.existsUserByUsername("username") } returns true

        val ex = assertThrows<IllegalArgumentException> {
            service.registerAndGetToken("username", "password")
        }
        assertEquals("User already exists", ex.message)
        verify { repo.existsUserByUsername("username") }
    }

    @Test
    fun `registerAndGetToken saves user and returns token`() {
        every { repo.existsUserByUsername("username") } returns false
        every { passwordEncoder.encode("password") } returns "encoded-password"
        every { repo.save(match { it.username == "username" && it.password == "encoded-password" }) } answers {
            firstArg<User>().copy(id = "id-1", tokenVersion = 0)
        }
        every { JwtUtil.generateToken("username", 0) } returns "jwt-token"

        val token = service.registerAndGetToken("username", "password")
        assertEquals("jwt-token", token)

        verifySequence {
            repo.existsUserByUsername("username")
            passwordEncoder.encode("password")
            repo.save(any())
            JwtUtil.generateToken("username", 0)
        }
    }

    @Test
    fun `loginAndGetToken throws on invalid credentials`() {
        every { repo.findByUsername("username") } returns null

        val ex = assertThrows<IllegalArgumentException> {
            service.loginAndGetToken("username", "password")
        }
        assertEquals("Invalid username or password", ex.message)
        verify { repo.findByUsername("username") }
    }

    @Test
    fun `loginAndGetToken returns token when credentials valid`() {
        val user = User(id = "u1", username = "username", password = "password", tokenVersion = 1)
        every { repo.findByUsername("username") } returns user
        every { passwordEncoder.matches("password", "password") } returns true
        every { JwtUtil.generateToken("username", 1) } returns "token"

        val token = service.loginAndGetToken("username", "password")
        assertEquals("token", token)

        verify { repo.findByUsername("username"); passwordEncoder.matches("password", "password"); JwtUtil.generateToken("username", 1) }
    }

    @Test
    fun `getUserData returns empty booleans when collectibles blank`() {
        val user = User(id = "u1", username = "username", password = "password", collectibles = null, tokenVersion = 0)

        every { JwtUtil.verifyAndGetInfo("token") } returns Pair("username", 0)
        every { repo.findByUsername("username") } returns user

        val unpacked = service.getUserData("token")
        assertEquals("username", unpacked.username)
        assertEquals(COLLECTIBLES_COUNT, unpacked.collectibles.size)
        assertTrue(unpacked.collectibles.all { it == false })

        verify { JwtUtil.verifyAndGetInfo("token"); repo.findByUsername("username") }
    }

    @Test
    fun `getUserData decodes collectibles when present`() {
        val encoded = "encoded-string"
        val user = User(id = "u1", username = "username", password = "password", collectibles = encoded, tokenVersion = 0)
        every { JwtUtil.verifyAndGetInfo("token") } returns Pair("username", 0)
        every { repo.findByUsername("username") } returns user
        val expected = BooleanArray(COLLECTIBLES_COUNT) { it % 2 == 0 }
        every { Base64Parser.stringToBooleans(encoded, COLLECTIBLES_COUNT) } returns expected

        val unpacked = service.getUserData("token")
        assertEquals("username", unpacked.username)
        assertArrayEquals(expected, unpacked.collectibles)

        verify { JwtUtil.verifyAndGetInfo("token"); repo.findByUsername("username"); Base64Parser.stringToBooleans(encoded, COLLECTIBLES_COUNT) }
    }

    @Test
    fun `updateCollectibles throws when wrong size`() {
        val small = BooleanArray(COLLECTIBLES_COUNT - 1) { false }
        val ex = assertThrows<IllegalArgumentException> {
            service.updateCollectibles("token", small)
        }
        assertTrue(ex.message!!.contains("Collectibles array must be"))
    }

    @Test
    fun `updateCollectibles saves encoded string`() {
        val arr = BooleanArray(COLLECTIBLES_COUNT) { it % 2 == 0 }
        every { JwtUtil.verifyAndGetInfo("token") } returns Pair("username", 0)
        val user = User(id = "u1", username = "username", password = "password", collectibles = null, tokenVersion = 0)
        every { repo.findByUsername("username") } returns user
        every { Base64Parser.booleansToString(arr) } returns "encoded-collectibles"
        every { repo.save(match { it.username == "username" && it.collectibles == "encoded-collectibles" }) } returns user.copy(collectibles = "encoded-collectibles")

        service.updateCollectibles("token", arr)

        verify { JwtUtil.verifyAndGetInfo("token"); repo.findByUsername("username"); Base64Parser.booleansToString(arr); repo.save(any()) }
    }

    @Test
    fun `updatePasswordAndGetToken throws when old password incorrect`() {
        val user = User(id = "u1", username = "username", password = "old-hash", tokenVersion = 1)
        every { JwtUtil.verifyAndGetInfo("token") } returns Pair("username", 1)
        every { repo.findByUsername("username") } returns user
        every { passwordEncoder.matches("wrong", "old-hash") } returns false

        val ex = assertThrows<IllegalArgumentException> {
            service.updatePasswordAndGetToken("token", "wrong", "new-password")
        }
        assertEquals("Old password is incorrect", ex.message)
        verify { JwtUtil.verifyAndGetInfo("token"); repo.findByUsername("username"); passwordEncoder.matches("wrong", "old-hash") }
    }

    @Test
    fun `updatePasswordAndGetToken encodes, saves and returns new token`() {
        val user = User(id = "u1", username = "username", password = "old-hash", tokenVersion = 1)
        every { JwtUtil.verifyAndGetInfo("token") } returns Pair("username", 1)
        every { repo.findByUsername("username") } returns user
        every { passwordEncoder.matches("old-password", "old-hash") } returns true
        every { passwordEncoder.encode("new-password") } returns "new-hash"

        val slot = slot<User>()
        every { repo.save(capture(slot)) } answers { slot.captured }
        every { JwtUtil.generateToken("username", 2) } returns "new-token"

        val newToken = service.updatePasswordAndGetToken("token", "old-password", "new-password")
        assertEquals("new-token", newToken)
        assertEquals("new-hash", slot.captured.password)
        assertEquals(2, slot.captured.tokenVersion)

        verify { JwtUtil.verifyAndGetInfo("token"); repo.findByUsername("username"); passwordEncoder.matches("old-password", "old-hash"); passwordEncoder.encode("new-password"); repo.save(any()); JwtUtil.generateToken("username", 2) }
    }

    @Test
    fun `deleteUser throws when token owner mismatch`() {
        val user = User(id = "u1", username = "username", password = "password", tokenVersion = 0)
        every { JwtUtil.verifyAndGetInfo("token") } returns Pair("username", 0)
        every { repo.findByUsername("username") } returns user

        val ex = assertThrows<IllegalArgumentException> {
            service.deleteUser("token", "other", "password")
        }
        assertEquals("Credentials do not match token owner", ex.message)
    }

    @Test
    fun `deleteUser validates credentials and deletes`() {
        val user = User(id = "u1", username = "username", password = "hash", tokenVersion = 0)
        every { JwtUtil.verifyAndGetInfo("token") } returns Pair("username", 0)
        every { repo.findByUsername("username") } returns user
        every { passwordEncoder.matches("password", "hash") } returns true
        every { repo.deleteByUsername("username") } just Runs

        service.deleteUser("token", "username", "password")

        verify { JwtUtil.verifyAndGetInfo("token"); repo.findByUsername("username"); passwordEncoder.matches("password", "hash"); repo.deleteByUsername("username") }
    }
}
