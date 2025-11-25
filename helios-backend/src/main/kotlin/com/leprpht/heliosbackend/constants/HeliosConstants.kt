package com.leprpht.heliosbackend.constants

const val COLLECTIBLES_COUNT = 155
const val LOGS_COUNT = 65
const val CIPHER_COUNT = 90

const val PASSWORD_MIN_LENGTH = 8
const val PASSWORD_MAX_LENGTH = 32
const val PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{$PASSWORD_MIN_LENGTH,$PASSWORD_MAX_LENGTH}\$"
const val USERNAME_MIN_LENGTH = 3
const val USERNAME_MAX_LENGTH = 32

val LOG_RANGES = listOf(
    0..16,
    27..35,
    56..65,
    81..91,
    107..116,
    127..134
)
val CIPHER_RANGES = listOf(
    17..26,
    36..55,
    66..80,
    92..106,
    117..126,
    135..154
)