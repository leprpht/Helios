package com.leprpht.heliosbackend.model

import com.leprpht.heliosbackend.util.Base64Parser
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "users")
data class User(
    @Id
    val id: String? = null,

    @Indexed(unique = true)
    val username: String,

    val password: String,

    val collectibles: String? = Base64Parser.booleansToString(BooleanArray(155) { false }),

    val tokenVersion: Int = 0
)
