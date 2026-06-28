package com.music.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.music.data.model.Comment
import com.music.data.model.Music
import com.music.data.repository.MusicRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailMusicViewModel(
    private val repository: MusicRepository
) : ViewModel() {

    private val _music = MutableStateFlow<Music?>(null)
    val music: StateFlow<Music?> = _music.asStateFlow()

    private val _comments = MutableStateFlow<List<Comment>>(emptyList())
    val comments: StateFlow<List<Comment>> = _comments.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    fun loadMusic(musicId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getMusicById(musicId).fold(
                onSuccess = { music ->
                    _music.value = music
                    loadComments(musicId)
                },
                onFailure = { error ->
                    _toastMessage.value = "Erro ao carregar: ${error.message}"
                    _error.value = "Erro ao carregar música: ${error.message}"
                    _isLoading.value = false
                }
            )
        }
    }

    fun loadComments(musicId: String) {
        viewModelScope.launch {
            repository.getCommentsByMusicId(musicId).fold(
                onSuccess = { comments ->
                    _comments.value = comments
                    _isLoading.value = false
                },
                onFailure = { error ->
                    _error.value = "Erro ao carregar comentários: ${error.message}"
                    _isLoading.value = false
                }
            )
        }
    }

    fun addComment(musicId: String, content: String, userName: String) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.addComment(musicId, content, userName).fold(
                onSuccess = { newComment ->
                    _comments.value = _comments.value + newComment
                    loadComments(musicId)
                    _error.value = null
                    _isLoading.value = false
                    _toastMessage.value = "Comentário adicionado com sucesso! ✅"
                },
                onFailure = { error ->
                    _error.value = "Erro ao adicionar comentário: ${error.message}"
                    _isLoading.value = false
                }
            )
        }
    }

    fun clearToastMessage() {
        _toastMessage.value = null
    }

    fun deleteComment(commentId: String) {  // 👈 MUDOU O NOME
        viewModelScope.launch {
            _isLoading.value = true

            // Precisa do musicId para remover o comentário
            val currentMusic = _music.value
            if (currentMusic == null) {
                _error.value = "Música não encontrada"
                _toastMessage.value = "Erro: música não encontrada"
                _isLoading.value = false
                return@launch
            }

            repository.removeComment(currentMusic.id!!, commentId).fold(
                onSuccess = { updatedMusic ->
                    _music.value = updatedMusic
                    _comments.value = updatedMusic.comments ?: emptyList()
                    _error.value = null
                    _isLoading.value = false
                    // 👇 MENSAGEM DE SUCESSO
                    _toastMessage.value = "Comentário deletado com sucesso! 🗑️"
                },
                onFailure = { error ->
                    _error.value = "Erro ao remover comentário: ${error.message}"
                    _isLoading.value = false
                    // 👇 MENSAGEM DE ERRO
                    _toastMessage.value = "Erro ao deletar: ${error.message}"
                }
            )
        }
    }

    fun clearError() {
        _error.value = null
    }
}