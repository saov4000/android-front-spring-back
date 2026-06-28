package com.music.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.music.data.model.Music
import com.music.data.network.RetrofitClient
import com.music.data.repository.MusicRepository
import com.music.presentation.components.ErrorDialog
import com.music.presentation.components.LoadingSpinner
import com.music.presentation.viewmodel.MusicListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicListScreen(
    onNavigateToDetail: (String) -> Unit,
    onNavigateToCreate: () -> Unit
) {
    val repository = MusicRepository(RetrofitClient.apiService)

    val viewModel: MusicListViewModel = viewModel(
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MusicListViewModel(repository) as T
            }
        }
    )

    val musics by viewModel.musics.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadMusics()
    }

    if (error != null) {
        ErrorDialog(
            message = error ?: "Erro desconhecido",
            onDismiss = { viewModel.clearError() }
        )
    }

    Scaffold(
        containerColor = Color(0xFF0B0E14),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Minha PlayList",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1A2A4A)
                ),
                actions = {
                    IconButton(onClick = onNavigateToCreate) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Adicionar Música",
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                isLoading -> {
                    LoadingSpinner()
                }
                musics.isEmpty() -> {
                    EmptyState()
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(musics) { music ->
                            MusicItemCard(
                                music = music,
                                onClick = {
                                    music.id?.let { id ->
                                        onNavigateToDetail(id)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MusicItemCard(
    music: Music,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1A2332)
        ),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(
            width = 1.dp,
            color = Color(0xFF2C3E6B)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Título
                Text(
                    text = music.title,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                // Artista
                Text(
                    text = "Artista: ${music.artist}",
                    color = Color.LightGray,
                    fontSize = 14.sp
                )

                // Álbum
                if (music.album != null) {
                    Text(
                        text = "Álbum: ${music.album}",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }

                // Gênero com borda arredondada
                if (music.genre != null) {
                    Surface(
                        modifier = Modifier.padding(top = 4.dp),
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFF2C3E6B),
                        border = BorderStroke(
                            width = 1.dp,
                            color = Color(0xFF4A7BB7)
                        )
                    ) {
                        Text(
                            text = "Gênero: ${music.genre}",
                            color = Color.White,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.PlayArrow,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Nenhuma música encontrada",
            color = Color.Gray,
            fontSize = 18.sp
        )
        Text(
            text = "Clique no botão + para adicionar",
            color = Color.Gray,
            fontSize = 14.sp
        )
    }
}