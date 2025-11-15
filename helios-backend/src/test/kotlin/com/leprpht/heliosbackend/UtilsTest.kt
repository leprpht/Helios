package com.leprpht.heliosbackend

import com.leprpht.heliosbackend.util.Base64Parser
import com.leprpht.heliosbackend.util.BooleanWrapper
import com.leprpht.heliosbackend.util.JwtUtil
import com.leprpht.heliosbackend.util.TokenUtil
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class UtilsTest {

    @Test
    fun `wrap and unwrap should preserve boolean arrays`() {
        val original = BooleanArray(16) { it % 2 == 0 }
        val bytes = BooleanWrapper.wrap(original)
        val result = BooleanWrapper.unwrap(bytes, original.size)
        assertArrayEquals(original, result)
    }

    @Test
    fun `wrap produces correct byte array length`() {
        val arr = BooleanArray(10) { false }
        val bytes = BooleanWrapper.wrap(arr)
        assertEquals(2, bytes.size)
    }

    @Test
    fun `unwrap with smaller size truncates extra bits`() {
        val arr = BooleanArray(16) { true }
        val bytes = BooleanWrapper.wrap(arr)
        val result = BooleanWrapper.unwrap(bytes, 8)
        assertEquals(8, result.size)
        assertTrue(result.all { it })
    }

    @Test
    fun `booleansToString and stringToBooleans round-trip correctly`() {
        val original = BooleanArray(13) { it % 3 == 0 }
        val encoded = Base64Parser.booleansToString(original)
        val decoded = Base64Parser.stringToBooleans(encoded, original.size)
        assertArrayEquals(original, decoded)
    }

    @Test
    fun `stringToBooleans throws on invalid base64`() {
        val invalid = "-"
        val ex = assertThrows<IllegalArgumentException> {
            Base64Parser.stringToBooleans(invalid, 5)
        }
        assertEquals("Invalid collectibles data", ex.message)
    }

    @Test
    fun `extractToken returns token from valid header`() {
        val token = "token"
        val header = "Bearer token"
        assertEquals(token, TokenUtil.extractToken(header))
    }

    @Test
    fun `extractToken throws when header is null`() {
        val ex = assertThrows<IllegalArgumentException> {
            TokenUtil.extractToken(null)
        }
        assertEquals("Authorization header is missing", ex.message)
    }

    @Test
    fun `extractToken throws when header has wrong format`() {
        val ex = assertThrows<IllegalArgumentException> {
            TokenUtil.extractToken("token")
        }
        assertEquals("Invalid authorization header format", ex.message)
    }

    @Test
    fun `generateToken and verifyAndGetInfo round-trip correctly`() {
        val username = "user"
        val tv = 3
        val token = JwtUtil.generateToken(username, tv)
        val info = JwtUtil.verifyAndGetInfo(token)
        assertEquals(username, info.first)
        assertEquals(tv, info.second)
    }

    @Test
    fun `verifyAndGetInfo throws for invalid token`() {
        assertThrows<com.auth0.jwt.exceptions.JWTVerificationException> {
            JwtUtil.verifyAndGetInfo("invalid")
        }
    }
}
