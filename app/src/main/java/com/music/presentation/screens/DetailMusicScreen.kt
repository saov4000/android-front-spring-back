package com.music.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.music.data.model.Comment
import com.music.data.model.Music
import com.music.data.network.RetrofitClient
import com.music.data.repository.MusicRepository
import com.music.presentation.components.CommentItem
import com.music.presentation.components.ErrorDialog
import com.music.presentation.components.LoadingSpinner
import com.music.presentation.viewmodel.DetailMusicViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailMusicScreen(
    musicId: String,
    onDelete: (String) -> Unit,
    onEdit: (Music) -> Unit,
    onBack: () -> Unit
) {

    var showDeleteCommentDialog by remember { mutableStateOf(false) }
    var commentToDelete by remember { mutableStateOf<Comment?>(null) }

    val repository = MusicRepository(RetrofitClient.apiService)

    val viewModel: DetailMusicViewModel = viewModel(
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return DetailMusicViewModel(repository) as T
            }
        }
    )

    var showDeleteConfirmation by remember { mutableStateOf(false) }
    val music by viewModel.music.collectAsState()
    val comments by viewModel.comments.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    var commentText by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf("") }
    val toastMessage by viewModel.toastMessage.collectAsState()

    // ✅ ADICIONADO: Estado para edição de comentário
    var showEditCommentDialog by remember { mutableStateOf(false) }
    var editingComment by remember { mutableStateOf<Comment?>(null) }
    var editCommentText by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(musicId) {
        try {
            println("=== DetailMusicScreen ===")
            println("Music ID: $musicId")
            viewModel.loadMusic(musicId)
        } catch (e: Exception) {
            println("❌ ERRO CRÍTICO: ${e.message}")
            e.printStackTrace()
        }
    }

    LaunchedEffect(musicId) {
        viewModel.loadComments(musicId)
    }

    LaunchedEffect(toastMessage) {
        toastMessage?.let { message ->
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = message,
                    duration = SnackbarDuration.Short
                )
                viewModel.clearToastMessage()
            }
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
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Detalhes da Música", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
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
                if (music != null) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Card da Música
                        item {
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
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(
                                        modifier = Modifier.weight(1f),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text(
                                            text = music?.title ?: "Título não disponível",
                                            style = MaterialTheme.typography.headlineSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                        Text(
                                            text = "Artista: ${music?.artist ?: "Artista não disponível"}",
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = Color.LightGray,
                                            fontSize = 18.sp
                                        )
                                        Text(
                                            text = "Álbum: ${music?.album ?: "N/A"}",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = Color.LightGray,
                                            fontSize = 18.sp
                                        )
                                        Text(
                                            text = "Gênero: ${music?.genre ?: "N/A"}",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = Color.LightGray,
                                            fontSize = 18.sp
                                        )
                                        Text(
                                            text = "Ano: ${music?.year ?: "N/A"}",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = Color.LightGray,
                                            fontSize = 18.sp
                                        )
                                    }

                                    Column(
                                        horizontalAlignment = Alignment.End,
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        IconButton(
                                            onClick = {
                                                music?.let { onEdit(it) }
                                            },
                                            modifier = Modifier.size(40.dp)
                                        ) {
                                            Icon(
                                                Icons.Default.Edit,
                                                contentDescription = "Editar música",
                                                tint = Color(0xFF4CAF50)
                                            )
                                        }

                                        IconButton(
                                            onClick = {
                                                showDeleteConfirmation = true
                                            },
                                            modifier = Modifier.size(40.dp)
                                        ) {
                                            Icon(
                                                Icons.Default.Delete,
                                                contentDescription = "Excluir música",
                                                tint = Color(0xFFE74C3C)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Adicionar Comentário
                        item {
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
                                        text = "Adicionar Comentário",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )

                                    OutlinedTextField(
                                        value = userName,
                                        onValueChange = { userName = it },
                                        label = { Text("Seu nome", color = Color.Gray) },
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
                                        value = commentText,
                                        onValueChange = { commentText = it },
                                        label = { Text("Comentário", color = Color.Gray) },
                                        modifier = Modifier.fillMaxWidth(),
                                        minLines = 3,
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

                                    Button(
                                        onClick = {
                                            if (commentText.isNotBlank() && userName.isNotBlank()) {
                                                viewModel.addComment(
                                                    musicId = musicId,
                                                    content = commentText,
                                                    userName = userName
                                                )
                                                commentText = ""
                                                userName = ""
                                                viewModel.loadComments(musicId)
                                            }
                                        },
                                        modifier = Modifier.fillMaxWidth(),
                                        enabled = commentText.isNotBlank() && userName.isNotBlank() && !isLoading,
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFF2C3E6B),
                                            disabledContainerColor = Color(0xFF1A2332)
                                        )
                                    ) {
                                        Text("Enviar Comentário", color = Color.White)
                                    }
                                }
                            }
                        }

                        // Título Comentários
                        item {
                            Text(
                                text = "Comentários (${comments.size})",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        items(comments) { comment ->
                            CommentItem(
                                comment = comment,
                                onDelete = { commentId ->
                                    commentToDelete = comment
                                    showDeleteCommentDialog = true  // 👈 SÓ ISSO
                                },
                                isOwner = true
                            )
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Música não encontrada",
                            color = Color.White,
                            fontSize = 18.sp
                        )
                    }
                }
            }
        }
    }

    if (showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = {
                showDeleteConfirmation = false
            },
            title = {
                Text("Excluir Música", color = Color.White)
            },
            text = {
                Text(
                    text = "Tem certeza que deseja excluir \"${music?.title}\"? Esta ação não pode ser desfeita.",
                    color = Color.White
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        music?.id?.let { onDelete(it) }
                        showDeleteConfirmation = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE74C3C)
                    )
                ) {
                    Text("Excluir", color = Color.White)
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showDeleteConfirmation = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1A2332)
                    )
                ) {
                    Text("Cancelar", color = Color.White)
                }
            },
            containerColor = Color(0xFF1A2332),
            titleContentColor = Color.White
        )
    }
    // 👇 ADICIONE ESTE DIÁLOGO DE CONFIRMAÇÃO
    if (showDeleteCommentDialog && commentToDelete != null) {
        AlertDialog(
            onDismissRequest = {
                showDeleteCommentDialog = false
                commentToDelete = null
            },
            title = {
                Text("Deletar Comentário", color = Color.White)
            },
            text = {
                Text(
                    text = "Tem certeza que deseja deletar este comentário?\n\n\"${commentToDelete?.content}\"",
                    color = Color.White
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        commentToDelete?.id?.let { commentId ->
                            viewModel.deleteComment(commentId)
                        }
                        showDeleteCommentDialog = false
                        commentToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE74C3C)
                    )
                ) {
                    Text("Deletar", color = Color.White)
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showDeleteCommentDialog = false
                        commentToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1A2332)
                    )
                ) {
                    Text("Cancelar", color = Color.White)
                }
            },
            containerColor = Color(0xFF1A2332),
            titleContentColor = Color.White
        )
    }
}



