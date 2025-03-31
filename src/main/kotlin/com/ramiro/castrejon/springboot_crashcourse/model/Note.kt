package com.ramiro.castrejon.springboot_crashcourse.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document("notes")
data class Note(
    val title: String,
    val content: String,
    val color: Long,
    val created_at: Instant,
    val ownerId: ObjectId,
    @Id val id: ObjectId = ObjectId.get()
)
