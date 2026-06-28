// presentation/viewmodel/MusicListViewModel.kt
package com.music.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.music.data.model.Music
import com.music.data.repository.MusicRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MusicListViewModel(
    private val repository: MusicRepository
) : ViewModel() {

    private val _musics = MutableStateFlow<List<Music>>(emptyList())
    val musics: StateFlow<List<Music>> = _musics.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadMusics() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = repository.getAllMusics()
                result.fold(
                    onSuccess = { musicsList ->
                        _musics.value = musicsList
                    },
                    onFailure = { error ->
                        _error.value = error.message
                    }
                )
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}