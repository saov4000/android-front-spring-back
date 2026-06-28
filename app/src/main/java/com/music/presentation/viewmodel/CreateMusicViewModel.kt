// presentation/viewmodel/CreateMusicViewModel.kt
package com.music.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.music.data.model.Music
import com.music.data.repository.MusicRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CreateMusicViewModel(
    private val repository: MusicRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _success = MutableStateFlow(false)
    val success: StateFlow<Boolean> = _success.asStateFlow()

    fun createMusic(music: Music) {
        viewModelScope.launch {
            _isLoading.value = true
            _success.value = false

            repository.createMusic(music).fold(
                onSuccess = {
                    _success.value = true
                },
                onFailure = { error ->
                    _error.value = error.message
                }
            )

            _isLoading.value = false
        }
    }

    fun clearError() {
        _error.value = null
        _success.value = false
    }
}