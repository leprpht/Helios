package com.leprpht.heliosbackend.util

import com.leprpht.heliosbackend.constants.*
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

     private fun extractSelectedBooleans(data: String, size: Int, ranges: List<IntRange>): BooleanArray {
        val allBooleans = stringToBooleans(data, size)
        val resultList = mutableListOf<Boolean>()
        for (range in ranges) {
            for (i in range) {
                if (i in allBooleans.indices) {
                    resultList.add(allBooleans[i])
                } else {
                    throw IndexOutOfBoundsException("Index $i is out of bounds for array of size $size")
                }
            }
        }
        return resultList.toBooleanArray()
    }

    fun extractLogs(data: String): BooleanArray =
        extractSelectedBooleans(data, COLLECTIBLES_COUNT, LOG_RANGES)

    fun extractCiphers(data: String): BooleanArray =
        extractSelectedBooleans(data, COLLECTIBLES_COUNT, CIPHER_RANGES)
}