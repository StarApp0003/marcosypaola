package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.PhotoAlbum
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.AddMemoryDialog
import com.example.ui.components.CoupleHeader
import com.example.ui.components.EditAnniversaryDialog
import com.example.ui.components.MediaViewerDialog
import com.example.ui.components.MemoryCard
import com.example.ui.components.LoveNotesSection
import com.example.ui.components.TimelineSection
import com.example.ui.theme.RoseRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: MemoryViewModel) {
    val coupleProfile by viewModel.coupleProfile.collectAsStateWithLifecycle()
    val allMemories by viewModel.allMemories.collectAsStateWithLifecycle()
    val filteredMemories by viewModel.filteredMemories.collectAsStateWithLifecycle()
    val selectedFilter by viewModel.selectedFilter.collectAsStateWithLifecycle()
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val loveNotes by viewModel.allLoveNotes.collectAsStateWithLifecycle()
    val activeMemory by viewModel.activeViewerMemory.collectAsStateWithLifecycle()

    var showAddMemoryDialog by remember { mutableStateOf(false) }
    var showEditAnniversaryDialog by remember { mutableStateOf(false) }

    val totalPhotos = remember(allMemories) { allMemories.count { it.mediaType == "PHOTO" } }
    val totalVideos = remember(allMemories) { allMemories.count { it.mediaType == "VIDEO" } }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${coupleProfile.partner1} & ${coupleProfile.partner2}",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Amor",
                            tint = RoseRed,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { showEditAnniversaryDialog = true },
                        modifier = Modifier.testTag("open_anniversary_settings_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "Configurar Aniversario",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier.navigationBarsPadding(),
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp
            ) {
                NavigationBarItem(
                    selected = currentTab == MainTab.MEMORIES,
                    onClick = { viewModel.setTab(MainTab.MEMORIES) },
                    icon = { Icon(imageVector = Icons.Default.PhotoAlbum, contentDescription = "Recuerdos") },
                    label = { Text("Recuerdos", fontWeight = FontWeight.SemiBold) },
                    modifier = Modifier.testTag("tab_memories"),
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )

                NavigationBarItem(
                    selected = currentTab == MainTab.TIMELINE,
                    onClick = { viewModel.setTab(MainTab.TIMELINE) },
                    icon = { Icon(imageVector = Icons.Default.Timeline, contentDescription = "Línea de Amor") },
                    label = { Text("Línea de Amor", fontWeight = FontWeight.SemiBold) },
                    modifier = Modifier.testTag("tab_timeline"),
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )

                NavigationBarItem(
                    selected = currentTab == MainTab.LOVE_NOTES,
                    onClick = { viewModel.setTab(MainTab.LOVE_NOTES) },
                    icon = { Icon(imageVector = Icons.Default.Mail, contentDescription = "Cartas") },
                    label = { Text("Cartitas", fontWeight = FontWeight.SemiBold) },
                    modifier = Modifier.testTag("tab_love_notes"),
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
            }
        },
        floatingActionButton = {
            if (currentTab == MainTab.MEMORIES) {
                FloatingActionButton(
                    onClick = { showAddMemoryDialog = true },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White,
                    shape = CircleShape,
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .testTag("add_memory_fab")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Agregar Recuerdo", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                MainTab.MEMORIES -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 96.dp)
                    ) {
                        // Header de Amor (Marcos & Paola, contador de días, quote)
                        item {
                            CoupleHeader(
                                profile = coupleProfile,
                                totalMemories = allMemories.size,
                                totalPhotos = totalPhotos,
                                totalVideos = totalVideos,
                                onEditAnniversary = { showEditAnniversaryDialog = true }
                            )
                        }

                        // Filter Chips Row
                        item {
                            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp)) {
                                Text(
                                    text = "Nuestros Recuerdos",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    items(MemoryFilter.entries) { filter ->
                                        val isSelected = selectedFilter == filter
                                        FilterChip(
                                            selected = isSelected,
                                            onClick = { viewModel.selectFilter(filter) },
                                            label = {
                                                Text("${filter.icon} ${filter.label}", fontWeight = FontWeight.Medium)
                                            },
                                            shape = RoundedCornerShape(14.dp),
                                            colors = FilterChipDefaults.filterChipColors(
                                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                                            ),
                                            modifier = Modifier.testTag("filter_chip_${filter.name.lowercase()}")
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))
                            }
                        }

                        // Empty State if no memories match
                        if (filteredMemories.isEmpty()) {
                            item {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(32.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(text = "📸", fontSize = 48.sp)
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = "No hay recuerdos en esta sección",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "Agrega fotos o videos juntos tocando en '+ Agregar Recuerdo'.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        } else {
                            // Memory Cards List
                            items(filteredMemories, key = { it.id }) { memory ->
                                MemoryCard(
                                    memory = memory,
                                    onClick = { viewModel.openViewer(memory) },
                                    onToggleFavorite = { viewModel.toggleFavorite(memory) },
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                )
                            }
                        }
                    }
                }

                MainTab.TIMELINE -> {
                    TimelineSection(
                        memories = allMemories,
                        onMemoryClick = { viewModel.openViewer(it) }
                    )
                }

                MainTab.LOVE_NOTES -> {
                    LoveNotesSection(
                        notes = loveNotes,
                        onAddNote = { sender, title, content, emoji ->
                            viewModel.addLoveNote(sender, title, content, emoji)
                        },
                        onDeleteNote = { viewModel.deleteLoveNote(it) }
                    )
                }
            }
        }
    }

    // Fullscreen Media Viewer Dialog
    activeMemory?.let { memory ->
        MediaViewerDialog(
            memory = memory,
            onDismiss = { viewModel.closeViewer() },
            onToggleFavorite = { viewModel.toggleFavorite(memory) },
            onDelete = {
                viewModel.deleteMemory(memory)
                viewModel.closeViewer()
            }
        )
    }

    // Add Memory Dialog
    if (showAddMemoryDialog) {
        AddMemoryDialog(
            onDismiss = { showAddMemoryDialog = false },
            onSaveMemory = { title, description, dateMillis, mediaUri, isVideo, category, quote ->
                viewModel.addMemory(title, description, dateMillis, mediaUri, isVideo, category, quote)
            },
            onSaveMediaFile = { uri, isVideo, callback ->
                viewModel.saveMediaFile(uri, isVideo, callback)
            }
        )
    }

    // Edit Anniversary Dialog
    if (showEditAnniversaryDialog) {
        EditAnniversaryDialog(
            profile = coupleProfile,
            onDismiss = { showEditAnniversaryDialog = false },
            onSave = { anniversaryMillis, quote ->
                viewModel.updateAnniversary(anniversaryMillis)
                viewModel.updateQuote(quote)
            }
        )
    }
}
