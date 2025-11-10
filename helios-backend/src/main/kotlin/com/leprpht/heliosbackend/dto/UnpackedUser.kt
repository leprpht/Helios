package com.leprpht.heliosbackend.dto

data class UnpackedUser(
    val username: String,
    val collectibles: BooleanArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UnpackedUser

        if (username != other.username) return false
        if (!collectibles.contentEquals(other.collectibles)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = username.hashCode()
        result = 31 * result + (collectibles?.contentHashCode() ?: 0)
        return result
    }
}
