package com.leprpht.heliosbackend.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class UpdatePasswordDto (

    @field:NotBlank(message = "Password must not be empty")
    @field:Size(min = 8, max = 32, message = "Password must be between 8 and 32 characters")
    @field:Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,32}\$",
        message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character"
    )
    val oldPassword: String,

    @field:NotBlank(message = "Password must not be empty")
    @field:Size(min = 8, max = 32, message = "Password must be between 8 and 32 characters")
    @field:Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,32}\$",
        message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character"
    )
    val newPassword: String
)