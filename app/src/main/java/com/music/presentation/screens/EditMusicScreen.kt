package com.music.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.music.data.model.Music
import com.music.data.network.RetrofitClient
import com.music.data.repository.MusicRepository
import com.music.presentation.components.ErrorDialog
import com.music.presentation.components.LoadingSpinner
import com.music.presentation.viewmodel.EditMusicViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditMusicScreen(
    musicId: String,
    onMusicUpdated: () -> Unit,
    onCancel: () -> Unit
) {
    val context = LocalContext.current
    val repository = MusicRepository(RetrofitClient.apiService)

    val viewModel: EditMusicViewModel = viewModel(
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return EditMusicViewModel(repository) as T
            }
        }
    )

    val uiState by viewModel.uiState.collectAsState()
    val currentMusic = uiState.music
    val isLoading = uiState.isLoading
    val error = uiState.error
    val isUpdated = uiState.isUpdated

    var title by remember { mutableStateOf("") }
    var artist by remember { mutableStateOf("") }
    var album by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }

    LaunchedEffect(musicId) {
        viewModel.loadMusic(musicId)
    }

    LaunchedEffect(currentMusic) {
        currentMusic?.let {
            title = it.title ?: ""
            artist = it.artist ?: ""
            album = it.album ?: ""
            genre = it.genre ?: ""
            year = it.year?.toString() ?: ""
        }
    }

    // ✅ ADICIONE O TOAST AQUI
    LaunchedEffect(isUpdated) {
        if (isUpdated) {
            Toast.makeText(context, "✅ Música atualizada com sucesso!", Toast.LENGTH_SHORT).show()
            onMusicUpdated()
        }
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
                title = { Text("Editar Música", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1A2A4A)
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (isLoading) {
                LoadingSpinner()
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
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
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "Editar Música",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            OutlinedTextField(
                                value = title,
                                onValueChange = { title = it },
                                label = { Text("Título", color = Color.Gray) },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF2C3E6B),
                                    unfocusedBorderColor = Color.Gray,
                                    cursorColor = Color.White,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedLabelColor = Color.White,
                                    unfocusedLabelColor = Color.Gray
                                )
                            )

                            OutlinedTextField(
                                value = artist,
                                onValueChange = { artist = it },
                                label = { Text("Artista", color = Color.Gray) },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF2C3E6B),
                                    unfocusedBorderColor = Color.Gray,
                                    cursorColor = Color.White,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedLabelColor = Color.White,
                                    unfocusedLabelColor = Color.Gray
                                )
                            )

                            OutlinedTextField(
                                value = album,
                                onValueChange = { album = it },
                                label = { Text("Álbum", color = Color.Gray) },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF2C3E6B),
                                    unfocusedBorderColor = Color.Gray,
                                    cursorColor = Color.White,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedLabelColor = Color.White,
                                    unfocusedLabelColor = Color.Gray
                                )
                            )

                            OutlinedTextField(
                                value = genre,
                                onValueChange = { genre = it },
                                label = { Text("Gênero", color = Color.Gray) },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF2C3E6B),
                                    unfocusedBorderColor = Color.Gray,
                                    cursorColor = Color.White,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedLabelColor = Color.White,
                                    unfocusedLabelColor = Color.Gray
                                )
                            )

                            OutlinedTextField(
                                value = year,
                                onValueChange = { year = it },
                                label = { Text("Ano", color = Color.Gray) },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF2C3E6B),
                                    unfocusedBorderColor = Color.Gray,
                                    cursorColor = Color.White,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedLabelColor = Color.White,
                                    unfocusedLabelColor = Color.Gray
                                )
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = {
                                        if (title.isNotBlank() && artist.isNotBlank()) {
                                            val updatedMusic = Music(
                                                id = musicId,
                                                title = title,
                                                artist = artist,
                                                album = album,
                                                genre = genre,
                                                year = year.toIntOrNull() ?: 0
                                            )
                                            viewModel.updateMusic(updatedMusic)
                                        }
                                    },
                                    modifier = Modifier.weight(1f),
                                    enabled = title.isNotBlank() && artist.isNotBlank() && !isLoading,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF2C3E6B)
                                    )
                                ) {
                                    Text("Salvar", color = Color.White)
                                }

                                Button(
                                    onClick = onCancel,
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF1A2332)
                                    )
                                ) {
                                    Text("Cancelar", color = Color.White)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}