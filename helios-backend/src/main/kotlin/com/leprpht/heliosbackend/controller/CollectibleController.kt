package com.leprpht.heliosbackend.controller

import com.leprpht.heliosbackend.service.HeliosService
import com.leprpht.heliosbackend.util.TokenUtil
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/collectibles")
class CollectibleController(private val heliosService: HeliosService) {

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

    @GetMapping("/data/{type}")
    fun getSpecificData(
        @RequestHeader("Authorization") token: String,
        @PathVariable type: String
    ): ResponseEntity<Any> {
        val cleanToken = TokenUtil.extractToken(token)
        when (type.lowercase()) {
            "logs" -> {
                return try {
                    val userData = heliosService.getUserLogs(cleanToken)
                    ResponseEntity.ok(userData)
                } catch (e: IllegalArgumentException) {
                    ResponseEntity.status(400).body(e.message)
                }
            }
            "ciphers" -> {
                return try {
                    val userData = heliosService.getUserCiphers(cleanToken)
                    ResponseEntity.ok(userData)
                } catch (e: IllegalArgumentException) {
                    ResponseEntity.status(400).body(e.message)
                }
            }
            else -> {
                return ResponseEntity.status(400).body("Invalid type")
            }
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
}