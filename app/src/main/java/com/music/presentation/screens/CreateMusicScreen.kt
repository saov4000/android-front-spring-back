package com.music.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.music.data.model.Music
import com.music.data.network.RetrofitClient
import com.music.data.repository.MusicRepository
import com.music.presentation.components.ErrorDialog
import com.music.presentation.components.LoadingSpinner
import com.music.presentation.viewmodel.CreateMusicViewModel
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateMusicScreen(
    onMusicCreated: () -> Unit,
    onCancel: () -> Unit
) {
    val context = LocalContext.current

    // Cria o repository
    val repository = MusicRepository(RetrofitClient.apiService)

    // Cria o ViewModel com Factory
    val viewModel: CreateMusicViewModel = viewModel(
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return CreateMusicViewModel(repository) as T
            }
        }
    )

    val isLoading by viewModel.isLoading.collectAsState(initial = false)
    val error: String? by viewModel.error.collectAsState(initial = null)
    val success: Boolean by viewModel.success.collectAsState(initial = false)

    var title by remember { mutableStateOf("") }
    var artist by remember { mutableStateOf("") }
    var album by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }

    // Exibe erro se houver
    if (error != null) {
        ErrorDialog(
            message = error ?: "Erro desconhecido",
            onDismiss = { viewModel.clearError() }
        )
    }

    // Quando criar com sucesso, mostra o Toast e volta para a tela anterior
    LaunchedEffect(success) {
        if (success) {
            // Mostra o Toast de sucesso
            Toast.makeText(
                context,
                "Música criada com sucesso!",
                Toast.LENGTH_SHORT
            ).show()

            // Pequeno delay para o usuário ver o Toast
            delay(500)
            onMusicCreated()
        }
    }

    Scaffold(
        containerColor = Color(0xFF0B0E14),
        topBar = {
            TopAppBar(
                title = { Text("Nova Música", color = Color.White) },
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
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Título *", color = Color.Gray) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF2C3E6B),
                            unfocusedBorderColor = Color.Gray,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.Gray,
                            cursorColor = Color.White,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )

                    OutlinedTextField(
                        value = artist,
                        onValueChange = { artist = it },
                        label = { Text("Artista *", color = Color.Gray) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF2C3E6B),
                            unfocusedBorderColor = Color.Gray,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.Gray,
                            cursorColor = Color.White,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
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
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.Gray,
                            cursorColor = Color.White,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
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
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.Gray,
                            cursorColor = Color.White,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
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
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.Gray,
                            cursorColor = Color.White,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        onClick = {
                            val yearInt = year.toIntOrNull()
                            val newMusic = Music(
                                title = title,
                                artist = artist,
                                album = album.takeIf { it.isNotBlank() },
                                genre = genre.takeIf { it.isNotBlank() },
                                year = yearInt,
                                comments = emptyList()
                            )
                            viewModel.createMusic(newMusic)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = title.isNotBlank() && artist.isNotBlank() && !isLoading,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2C3E6B),
                            disabledContainerColor = Color(0xFF1A2332)
                        )
                    ) {
                        Text(
                            text = "Criar Música",
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}