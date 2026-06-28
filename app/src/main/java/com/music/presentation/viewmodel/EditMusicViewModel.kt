package com.music.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.music.data.model.Music
import com.music.data.repository.MusicRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EditMusicViewModel(
    private val repository: MusicRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MusicEditUiState())
    val uiState: StateFlow<MusicEditUiState> = _uiState.asStateFlow()

    fun loadMusic(id: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val result = repository.getMusicById(id)
            result.fold(
                onSuccess = { music ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        music = music,
                        error = null
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message ?: "Erro ao carregar música"
                    )
                }
            )
        }
    }

    fun updateMusic(music: Music) {  // 👈 Agora recebe Music diretamente
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            music.id?.let { id ->
                val result = repository.updateMusic(id, music)
                result.fold(
                    onSuccess = { updatedMusic ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isUpdated = true,
                            music = updatedMusic,
                            error = null
                        )
                    },
                    onFailure = { error ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = error.message ?: "Erro ao atualizar música"
                        )
                    }
                )
            }
        }
    }

    fun deleteMusic(id: String) {  // 👈 Adiciona método para deletar
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val result = repository.deleteMusic(id)
            result.fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isDeleted = true,
                        error = null
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message ?: "Erro ao deletar música"
                    )
                }
            )
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun resetUpdateStatus() {
        _uiState.value = _uiState.value.copy(isUpdated = false, isDeleted = false)
    }
}

data class MusicEditUiState(
    val isLoading: Boolean = false,
    val music: Music? = null,
    val error: String? = null,
    val isUpdated: Boolean = false,
    val isDeleted: Boolean = false
)