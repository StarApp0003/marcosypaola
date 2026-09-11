package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "memories")
data class MemoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val dateMillis: Long,
    val mediaUri: String, // "res:img_couple_hero", "res:img_memory_date", "res:img_love_letter" or "file://..." / "content://..."
    val mediaType: String, // "PHOTO" or "VIDEO"
    val isFavorite: Boolean = false,
    val category: String = "Cita Romántica",
    val romanticQuote: String = ""
)
