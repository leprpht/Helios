package com.leprpht.heliosbackend.dto

import com.leprpht.heliosbackend.constants.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class LoginData(

    @field:NotBlank(message = "Username must not be empty")
    @field:Size(min = USERNAME_MIN_LENGTH, max = USERNAME_MAX_LENGTH, message = "Username must be between 3 and 32 characters")
    val username: String,

    @field:NotBlank(message = "Password must not be empty")
    @field:Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH, message = "Password must be between 8 and 32 characters")
    @field:Pattern(
        regexp = PASSWORD_REGEX,
        message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character"
    )
    val password: String
)
