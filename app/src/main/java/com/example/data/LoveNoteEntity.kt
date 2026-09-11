package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "love_notes")
data class LoveNoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val sender: String, // "Marcos", "Paola", or "Juntos"
    val title: String,
    val content: String,
    val emoji: String = "❤️",
    val dateMillis: Long = System.currentTimeMillis()
)
