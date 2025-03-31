package com.ramiro.castrejon.springboot_crashcourse.data.repository

import com.ramiro.castrejon.springboot_crashcourse.model.User
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository


interface UserRepository: MongoRepository<User, ObjectId> {
    fun findByEmail(email: String): User?
}