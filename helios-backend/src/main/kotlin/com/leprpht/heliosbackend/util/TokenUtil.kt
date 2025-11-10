package com.leprpht.heliosbackend.util

object TokenUtil {
    fun extractToken(authorizationHeader: String?): String {
        if (authorizationHeader == null) throw IllegalArgumentException("Authorization header is missing")
        if (!authorizationHeader.startsWith("Bearer ")) throw IllegalArgumentException("Invalid authorization header format")

        return authorizationHeader.substringAfter("Bearer ").trim()
    }
}