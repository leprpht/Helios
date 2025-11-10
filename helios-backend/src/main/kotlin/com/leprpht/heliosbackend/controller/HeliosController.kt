package com.leprpht.heliosbackend.controller

import com.leprpht.heliosbackend.dto.LoginData
import com.leprpht.heliosbackend.dto.UpdatePasswordDto
import com.leprpht.heliosbackend.service.HeliosService
import com.leprpht.heliosbackend.util.TokenUtil
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class HeliosController(private val heliosService: HeliosService) {

    @PostMapping("/register")
    fun register(
        @RequestBody loginData: LoginData
    ): ResponseEntity<Any> {
        return try {
            val token = heliosService.registerAndGetToken(loginData.username, loginData.password)
            ResponseEntity.ok(mapOf("token" to token))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(400).body(e.message)
        }
    }

    @PostMapping("/login")
    fun login(
        @RequestBody loginData: LoginData
    ): ResponseEntity<Any> {
        return try {
            val token = heliosService.loginAndGetToken(loginData.username, loginData.password)
            ResponseEntity.ok(mapOf("token" to token))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(400).body(e.message)
        }
    }

    @GetMapping("/data")
    fun getData(
        @RequestHeader("Authorization") token: String
    ): ResponseEntity<Any> {
        val cleanToken = TokenUtil.extractToken(token)
        return try {
            val userData = heliosService.getUserData(cleanToken)
            ResponseEntity.ok(userData)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(400).body(e.message)
        }
    }

    @PutMapping("/update-collectibles")
    fun updateCollectibles(
        @RequestHeader("Authorization") token: String,
        @RequestBody collectibles: BooleanArray
    ): ResponseEntity<Any> {
        val cleanToken = TokenUtil.extractToken(token)
        return try {
            heliosService.updateCollectibles(cleanToken, collectibles)
            ResponseEntity.ok("Collectibles updated successfully")
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(400).body(e.message)
        }
    }

    @PutMapping("/update-password")
    fun updatePassword(
        @RequestHeader("Authorization") token: String,
        @RequestBody passwordUpdate: UpdatePasswordDto
    ): ResponseEntity<Any> {
        val cleanToken = TokenUtil.extractToken(token)
        return try {
            val newToken = heliosService.updatePasswordAndGetToken(cleanToken, passwordUpdate.oldPassword, passwordUpdate.newPassword)
            ResponseEntity.ok(mapOf("token" to newToken))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(400).body(e.message)
        }
    }

    @DeleteMapping("/delete-account")
    fun deleteAccount(
        @RequestHeader("Authorization") token: String,
        @RequestBody loginData: LoginData
    ): ResponseEntity<Any> {
        val cleanToken = TokenUtil.extractToken(token)
        return try {
            heliosService.deleteUser(cleanToken, loginData.username, loginData.password)
            ResponseEntity.ok("Account deleted successfully")
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(400).body(e.message)
        }
    }
}