package com.leprpht.heliosbackend.dto

data class UpdatePasswordDto (
    val oldPassword: String,
    val newPassword: String
)