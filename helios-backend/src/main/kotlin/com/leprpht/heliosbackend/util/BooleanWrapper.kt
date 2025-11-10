package com.leprpht.heliosbackend.util

import kotlin.experimental.or

object BooleanWrapper {
    fun wrap(values: BooleanArray): ByteArray {
        val byteArray = ByteArray((values.size + 7) / 8)
        for (i in values.indices) {
            if (values[i]) {
                byteArray[i / 8] = byteArray[i / 8] or (1 shl (i % 8)).toByte()
            }
        }
        return byteArray
    }

    fun unwrap(byteArray: ByteArray, size: Int): BooleanArray {
        val values = BooleanArray(size)
        for (i in 0 until size) {
            values[i] = (byteArray[i / 8].toInt() and (1 shl (i % 8))) != 0
        }
        return values
    }
}