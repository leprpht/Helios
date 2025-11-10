package com.leprpht.heliosbackend.repository

import com.leprpht.heliosbackend.model.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface HeliosRepository : MongoRepository<User, String> {
    fun findByUsername(username: String): User?
    fun existsUserByUsername(username: String): Boolean
    fun deleteByUsername(username: String)
}