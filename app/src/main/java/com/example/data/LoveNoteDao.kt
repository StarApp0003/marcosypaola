package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LoveNoteDao {
    @Query("SELECT * FROM love_notes ORDER BY dateMillis DESC")
    fun getAllNotes(): Flow<List<LoveNoteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: LoveNoteEntity): Long

    @Delete
    suspend fun deleteNote(note: LoveNoteEntity)

    @Query("SELECT COUNT(*) FROM love_notes")
    suspend fun getNotesCount(): Int
}
