package com.leprpht.heliosbackend

import com.leprpht.heliosbackend.dto.LoginData
import com.leprpht.heliosbackend.dto.UpdatePasswordDto
import com.leprpht.heliosbackend.dto.UnpackedUser
import com.leprpht.heliosbackend.repository.HeliosRepository
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.*
import kotlin.test.assertTrue

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class HeliosIntegrationTest {

    @LocalServerPort
    private var port: Int = 0

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Autowired
    lateinit var repo: HeliosRepository

    private fun baseUrl(path: String) = "http://localhost:$port/api$path"

    @BeforeEach
    fun setup() {
        repo.deleteAll()
    }

    @AfterEach
    fun tearDown() {
        repo.deleteAll()
    }

    private fun registerAndGetToken(username: String, password: String): String {
        val registerData = LoginData(username = username, password = password)
        val response = restTemplate.postForEntity(baseUrl("/auth/register"), registerData, Map::class.java)
        Assertions.assertEquals(HttpStatus.OK, response.statusCode)
        val token = response.body?.get("token") as String
        assertTrue(token.isNotEmpty())
        return token
    }

    @Test
    fun `register user returns token`() {
        registerAndGetToken("user", "password")
    }

    @Test
    fun `login user returns token`() {
        registerAndGetToken("user", "password")
        val loginData = LoginData(username = "user", password = "password")
        val response = restTemplate.postForEntity(baseUrl("/auth/login"), loginData, Map::class.java)
        Assertions.assertEquals(HttpStatus.OK, response.statusCode)
        val token = response.body?.get("token") as String
        assertTrue(token.isNotEmpty())
    }

    @Test
    fun `get user data returns correct username and collectibles`() {
        val token = registerAndGetToken("user", "password")

        val headers = HttpHeaders()
        headers.set("Authorization", "Bearer $token")
        val entity = HttpEntity<String>(headers)

        val response = restTemplate.exchange(
            baseUrl("/collectibles/data"),
            HttpMethod.GET,
            entity,
            UnpackedUser::class.java
        )
        Assertions.assertEquals(HttpStatus.OK, response.statusCode)
        val userData = response.body!!
        Assertions.assertEquals("user", userData.username)
        Assertions.assertEquals(155, userData.collectibles.size)
    }

    @Test
    fun `update collectibles updates correctly`() {
        val token = registerAndGetToken("user", "password")

        val headers = HttpHeaders()
        headers.set("Authorization", "Bearer $token")
        val newCollectibles = BooleanArray(155) { it % 2 == 0 }
        val entity = HttpEntity(newCollectibles, headers)

        val response = restTemplate.exchange(
            baseUrl("/collectibles/update-collectibles"),
            HttpMethod.PUT,
            entity,
            String::class.java
        )
        Assertions.assertEquals(HttpStatus.OK, response.statusCode)
    }

    @Test
    fun `update password returns new token`() {
        val token = registerAndGetToken("user", "old-password")

        val headers = HttpHeaders()
        headers.set("Authorization", "Bearer $token")
        val dto = UpdatePasswordDto(oldPassword = "old-password", newPassword = "new-password")
        val entity = HttpEntity(dto, headers)

        val response = restTemplate.exchange(
            baseUrl("/auth/update-password"),
            HttpMethod.PUT,
            entity,
            Map::class.java
        )
        Assertions.assertEquals(HttpStatus.OK, response.statusCode)
        val newToken = response.body?.get("token") as String
        assertTrue(newToken.isNotEmpty())
    }

    @Test
    fun `delete user removes from database`() {
        val token = registerAndGetToken("user", "password")

        val headers = HttpHeaders()
        headers.set("Authorization", "Bearer $token")
        val loginData = LoginData(username = "user", password = "password")
        val entity = HttpEntity(loginData, headers)

        val response = restTemplate.exchange(
            baseUrl("/auth/delete-account"),
            HttpMethod.DELETE,
            entity,
            String::class.java
        )
        Assertions.assertEquals(HttpStatus.OK, response.statusCode)
        Assertions.assertNull(repo.findByUsername("user"))
    }
}
