package com.leprpht.heliosbackend.util

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException

object JwtUtil {
    private val secret = System.getenv("JWT_SECRET")
        ?: throw IllegalStateException("JWT_SECRET not set in environment")
    private val algorithm = Algorithm.HMAC256(secret)

    fun generateToken(username: String, tokenVersion: Int): String {
        return JWT.create()
            .withSubject(username)
            .withClaim("tv", tokenVersion)
            .sign(algorithm)
    }

    @Throws(JWTVerificationException::class)
    fun verifyAndGetInfo(token: String): Pair<String, Int> {
        val verifier = JWT.require(algorithm).build()
        val jwt = verifier.verify(token)
        val username = jwt.subject
        val tv = jwt.getClaim("tv").asInt() ?: 0
        return username to tv
    }
}