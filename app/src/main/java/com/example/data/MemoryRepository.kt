package com.example.data

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class MemoryRepository(
    private val context: Context,
    private val memoryDao: MemoryDao,
    private val loveNoteDao: LoveNoteDao,
    val couplePreferences: CouplePreferences
) {
    val allMemories: Flow<List<MemoryEntity>> = memoryDao.getAllMemories()
    val allLoveNotes: Flow<List<LoveNoteEntity>> = loveNoteDao.getAllNotes()

    suspend fun addMemory(memory: MemoryEntity): Long {
        return memoryDao.insertMemory(memory)
    }

    suspend fun updateMemory(memory: MemoryEntity) {
        memoryDao.updateMemory(memory)
    }

    suspend fun deleteMemory(memory: MemoryEntity) {
        // If it's an internal file, delete the file too
        if (memory.mediaUri.startsWith("file://")) {
            try {
                val path = memory.mediaUri.removePrefix("file://")
                val file = File(path)
                if (file.exists()) {
                    file.delete()
                }
            } catch (_: Exception) {}
        }
        memoryDao.deleteMemory(memory)
    }

    suspend fun toggleFavorite(memory: MemoryEntity) {
        memoryDao.updateMemory(memory.copy(isFavorite = !memory.isFavorite))
    }

    suspend fun addLoveNote(note: LoveNoteEntity): Long {
        return loveNoteDao.insertNote(note)
    }

    suspend fun deleteLoveNote(note: LoveNoteEntity) {
        loveNoteDao.deleteNote(note)
    }

    suspend fun saveMediaToInternalStorage(uri: Uri, isVideo: Boolean): String = withContext(Dispatchers.IO) {
        val extension = if (isVideo) "mp4" else "jpg"
        val fileName = "memory_${System.currentTimeMillis()}_${UUID.randomUUID().toString().take(6)}.$extension"
        val mediaDir = File(context.filesDir, "media").apply { if (!exists()) mkdirs() }
        val destFile = File(mediaDir, fileName)

        context.contentResolver.openInputStream(uri)?.use { input ->
            FileOutputStream(destFile).use { output ->
                input.copyTo(output)
            }
        }
        "file://${destFile.absolutePath}"
    }
}
