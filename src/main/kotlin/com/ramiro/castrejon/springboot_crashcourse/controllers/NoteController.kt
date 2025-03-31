package com.ramiro.castrejon.springboot_crashcourse.controllers

import com.ramiro.castrejon.springboot_crashcourse.controllers.NoteController.NoteResponse
import com.ramiro.castrejon.springboot_crashcourse.data.repository.NoteRepository
import com.ramiro.castrejon.springboot_crashcourse.model.Note
import org.bson.types.ObjectId
import org.springframework.web.bind.annotation.*
import java.time.Instant

@RestController
@RequestMapping("/notes")
class NoteController(
    private val noteRepository: NoteRepository
) {

    data class NoteRequest(
        val id: String?,
        val title: String,
        val content : String,
        val color: Long
    )

    data class NoteResponse(
        val id: String,
        val title: String,
        val content: String,
        val color: Long,
        val created_at: Instant
    )

    @PostMapping
    fun save(
        @RequestBody body: NoteRequest) : NoteResponse {
        val note = noteRepository.save(
            Note(
                id = body.id?.let { ObjectId(it) } ?: ObjectId.get(),
                title = body.title,
                content = body.content,
                color = body.color,
                created_at = Instant.now(),
                ownerId = ObjectId()
            )
        )

        return note.toResponse()
    }

    @GetMapping
    fun findByOwnerId(
        @RequestParam("ownerId") ownerId: String
    ): List<NoteResponse> {
        return noteRepository.findByOwnerId(
            ObjectId(ownerId)
        ).map {
            it.toResponse()
        }
    }

    @DeleteMapping(path = ["/{id}"])
    fun deleteById(@PathVariable("id") id: String) {
        noteRepository.deleteById(ObjectId(id))
    }
}

private fun Note.toResponse():NoteController.NoteResponse {
    return NoteResponse(
        id = id.toHexString(),
        title = title,
        content = content,
        color = color,
        created_at = created_at
    )
}