package com.leprpht.heliosbackend.dto

import com.leprpht.heliosbackend.constants.PASSWORD_MAX_LENGTH
import com.leprpht.heliosbackend.constants.PASSWORD_MIN_LENGTH
import com.leprpht.heliosbackend.constants.PASSWORD_REGEX
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class UpdatePasswordDto (

    @field:NotBlank(message = "Password must not be empty")
    @field:Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH, message = "Password must be between 8 and 32 characters")
    @field:Pattern(
        regexp = PASSWORD_REGEX,
        message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character"
    )
    val oldPassword: String,

    @field:NotBlank(message = "Password must not be empty")
    @field:Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH, message = "Password must be between 8 and 32 characters")
    @field:Pattern(
        regexp = PASSWORD_REGEX,
        message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character"
    )
    val newPassword: String
)