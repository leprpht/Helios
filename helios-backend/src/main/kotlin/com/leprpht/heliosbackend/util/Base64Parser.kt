package com.leprpht.heliosbackend.util

import java.util.Base64

object Base64Parser {
    fun booleansToString(values: BooleanArray): String {
        val byteArray = BooleanWrapper.wrap(values)
        return Base64.getEncoder().encodeToString(byteArray)
    }

    fun stringToBooleans(data: String, size: Int): BooleanArray {
        try {
            val byteArray = Base64.getDecoder().decode(data)
            return BooleanWrapper.unwrap(byteArray, size)
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("Invalid collectibles data")
        }
    }

}