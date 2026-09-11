package com.example.ui

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.CouplePreferences
import com.example.data.CoupleProfile
import com.example.data.LoveNoteEntity
import com.example.data.MemoryEntity
import com.example.data.MemoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class MemoryFilter(val label: String, val icon: String) {
    ALL("Todos", "✨"),
    PHOTOS("Fotos", "📸"),
    VIDEOS("Videos", "🎥"),
    FAVORITES("Favoritos", "❤️")
}

enum class MainTab(val title: String) {
    MEMORIES("Recuerdos"),
    TIMELINE("Línea de Amor"),
    LOVE_NOTES("Notas de Amor")
}

class MemoryViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: MemoryRepository

    val coupleProfile: StateFlow<CoupleProfile>
    val allMemories: StateFlow<List<MemoryEntity>>
    val allLoveNotes: StateFlow<List<LoveNoteEntity>>

    private val _selectedFilter = MutableStateFlow(MemoryFilter.ALL)
    val selectedFilter: StateFlow<MemoryFilter> = _selectedFilter.asStateFlow()

    private val _currentTab = MutableStateFlow(MainTab.MEMORIES)
    val currentTab: StateFlow<MainTab> = _currentTab.asStateFlow()

    private val _activeViewerMemory = MutableStateFlow<MemoryEntity?>(null)
    val activeViewerMemory: StateFlow<MemoryEntity?> = _activeViewerMemory.asStateFlow()

    val filteredMemories: StateFlow<List<MemoryEntity>>

    init {
        val database = AppDatabase.getDatabase(application)
        val preferences = CouplePreferences(application)
        repository = MemoryRepository(
            context = application,
            memoryDao = database.memoryDao(),
            loveNoteDao = database.loveNoteDao(),
            couplePreferences = preferences
        )

        coupleProfile = preferences.profileFlow

        allMemories = repository.allMemories.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        allLoveNotes = repository.allLoveNotes.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        filteredMemories = combine(allMemories, _selectedFilter) { list, filter ->
            when (filter) {
                MemoryFilter.ALL -> list
                MemoryFilter.PHOTOS -> list.filter { it.mediaType == "PHOTO" }
                MemoryFilter.VIDEOS -> list.filter { it.mediaType == "VIDEO" }
                MemoryFilter.FAVORITES -> list.filter { it.isFavorite }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    }

    fun selectFilter(filter: MemoryFilter) {
        _selectedFilter.value = filter
    }

    fun setTab(tab: MainTab) {
        _currentTab.value = tab
    }

    fun openViewer(memory: MemoryEntity) {
        _activeViewerMemory.value = memory
    }

    fun closeViewer() {
        _activeViewerMemory.value = null
    }

    fun toggleFavorite(memory: MemoryEntity) {
        viewModelScope.launch {
            repository.toggleFavorite(memory)
        }
    }

    fun addMemory(
        title: String,
        description: String,
        dateMillis: Long,
        mediaUri: String,
        isVideo: Boolean,
        category: String,
        romanticQuote: String
    ) {
        viewModelScope.launch {
            val entity = MemoryEntity(
                title = title,
                description = description,
                dateMillis = dateMillis,
                mediaUri = mediaUri,
                mediaType = if (isVideo) "VIDEO" else "PHOTO",
                isFavorite = false,
                category = category,
                romanticQuote = romanticQuote
            )
            repository.addMemory(entity)
        }
    }

    fun deleteMemory(memory: MemoryEntity) {
        viewModelScope.launch {
            if (_activeViewerMemory.value?.id == memory.id) {
                _activeViewerMemory.value = null
            }
            repository.deleteMemory(memory)
        }
    }

    fun saveMediaFile(uri: Uri, isVideo: Boolean, onComplete: (String) -> Unit) {
        viewModelScope.launch {
            val savedPath = repository.saveMediaToInternalStorage(uri, isVideo)
            onComplete(savedPath)
        }
    }

    fun addLoveNote(sender: String, title: String, content: String, emoji: String) {
        viewModelScope.launch {
            repository.addLoveNote(
                LoveNoteEntity(
                    sender = sender,
                    title = title,
                    content = content,
                    emoji = emoji
                )
            )
        }
    }

    fun deleteLoveNote(note: LoveNoteEntity) {
        viewModelScope.launch {
            repository.deleteLoveNote(note)
        }
    }

    fun updateAnniversary(millis: Long) {
        repository.couplePreferences.updateAnniversary(millis)
    }

    fun updatePartners(p1: String, p2: String) {
        repository.couplePreferences.updatePartners(p1, p2)
    }

    fun updateQuote(quote: String) {
        repository.couplePreferences.updateQuote(quote)
    }
}
