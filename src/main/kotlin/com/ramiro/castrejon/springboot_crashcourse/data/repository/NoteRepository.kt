package com.ramiro.castrejon.springboot_crashcourse.data.repository

import com.ramiro.castrejon.springboot_crashcourse.model.Note
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface NoteRepository: MongoRepository<Note, ObjectId> {
    fun findByOwnerId(ownerId: ObjectId): List<Note>
}