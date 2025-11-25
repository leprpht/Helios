package com.leprpht.heliosbackend.service

import com.leprpht.heliosbackend.constants.*
import com.leprpht.heliosbackend.dto.UnpackedUser
import com.leprpht.heliosbackend.model.User
import com.leprpht.heliosbackend.repository.HeliosRepository
import com.leprpht.heliosbackend.util.Base64Parser
import com.leprpht.heliosbackend.util.JwtUtil
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class HeliosService(
    private val heliosRepository: HeliosRepository,
    private val passwordEncoder: PasswordEncoder
) {
    fun registerAndGetToken(username: String, password: String): String {
        if (heliosRepository.existsUserByUsername(username))
            throw IllegalArgumentException("User already exists")

        val encodedPassword = passwordEncoder.encode(password)
        val user = User(
            username = username,
            password = encodedPassword
        )
        val saved = heliosRepository.save(user)

        return JwtUtil.generateToken(saved.username, saved.tokenVersion)
    }

    fun loginAndGetToken(username: String, password: String): String {
        val user = validateCredentials(username, password)
        return JwtUtil.generateToken(username, user.tokenVersion)
    }

    fun getUserData(token: String): UnpackedUser {
        val user = verifyTokenAndGetUser(token)
        val data = try {
            if (user.collectibles.isNullOrBlank()) {
                BooleanArray(COLLECTIBLES_COUNT) { false }
            } else {
                Base64Parser.stringToBooleans(user.collectibles, COLLECTIBLES_COUNT)
            }
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("Stored collectibles are invalid")
        }

        return UnpackedUser(
            username = user.username,
            collectibles = data
        )
    }

    fun getUserLogs(token: String): UnpackedUser {
        val user = verifyTokenAndGetUser(token)
        val data = try {
            if (user.collectibles.isNullOrBlank()) {
                BooleanArray(COLLECTIBLES_COUNT) { false }
            } else {
                Base64Parser.extractLogs(user.collectibles)
            }
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("Stored collectibles are invalid")
        }

        return UnpackedUser(
            username = user.username,
            collectibles = data
        )
    }

    fun getUserCiphers(token: String): UnpackedUser {
        val user = verifyTokenAndGetUser(token)
        val data = try {
            if (user.collectibles.isNullOrBlank()) {
                BooleanArray(COLLECTIBLES_COUNT) { false }
            } else {
                Base64Parser.extractCiphers(user.collectibles)
            }
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("Stored collectibles are invalid")
        }

        return UnpackedUser(
            username = user.username,
            collectibles = data
        )
    }

    fun updateCollectibles(token: String, collectibles: BooleanArray) {
        if (collectibles.size != COLLECTIBLES_COUNT)
            throw IllegalArgumentException("Collectibles array must be $COLLECTIBLES_COUNT long")

        val user = verifyTokenAndGetUser(token)
        val encodedCollectibles = Base64Parser.booleansToString(collectibles)

        val updatedUser = user.copy(collectibles = encodedCollectibles)
        heliosRepository.save(updatedUser)
    }

    fun updatePasswordAndGetToken(token: String, oldPassword: String, newPassword: String): String {
        val user = verifyTokenAndGetUser(token)

        if (!passwordEncoder.matches(oldPassword, user.password))
            throw IllegalArgumentException("Old password is incorrect")

        val newEncodedPassword = passwordEncoder.encode(newPassword)
        val updatedUser = user.copy(
            password = newEncodedPassword,
            tokenVersion = user.tokenVersion + 1
        )
        heliosRepository.save(updatedUser)

        return JwtUtil.generateToken(updatedUser.username, updatedUser.tokenVersion)
    }

    fun deleteUser(token: String, username: String, password: String) {
        val user = verifyTokenAndGetUser(token)

        if (user.username != username)
            throw IllegalArgumentException("Credentials do not match token owner")

        validateCredentials(username, password)
        heliosRepository.deleteByUsername(username)
    }

    private fun validateCredentials(username: String, password: String): User {
        val user = heliosRepository.findByUsername(username)
            ?: throw IllegalArgumentException("Invalid username or password")

        if (!passwordEncoder.matches(password, user.password))
            throw IllegalArgumentException("Invalid username or password")

        return user
    }

    private fun verifyTokenAndGetUser(token: String): User {
        val (username, tokenVersion) = JwtUtil.verifyAndGetInfo(token)
        val user = heliosRepository.findByUsername(username)
            ?: throw IllegalArgumentException("Invalid token")

        if (user.tokenVersion != tokenVersion) {
            throw IllegalArgumentException("Invalid token")
        }

        return user
    }
}