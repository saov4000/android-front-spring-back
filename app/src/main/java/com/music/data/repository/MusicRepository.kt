package com.music.data.repository

import com.music.data.model.Music
import com.music.data.model.Comment
import com.music.data.network.ApiService
import retrofit2.Response

class MusicRepository(
    private val apiService: ApiService
) {

    private suspend fun <T> handleApiCall(
        call: suspend () -> Response<T>
    ): Result<T> {
        return try {
            val response = call()
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Resposta vazia"))
            } else {
                Result.failure(Exception("Erro: ${response.code()} - ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception(e.message ?: "Erro desconhecido"))
        }
    }

    suspend fun getAllMusics(): Result<List<Music>> {
        return handleApiCall { apiService.getMusics() }
    }

    suspend fun getMusicById(id: String): Result<Music> {
        return handleApiCall { apiService.getMusicById(id) }
    }

    suspend fun createMusic(music: Music): Result<Music> {
        return handleApiCall { apiService.createMusic(music) }
    }

    suspend fun updateMusic(id: String, music: Music): Result<Music> {
        return handleApiCall { apiService.updateMusic(id, music) }
    }

    suspend fun deleteMusic(id: String): Result<Unit> {
        return handleApiCall { apiService.deleteMusic(id) }
    }

    // CORRIGIDO: Agora retorna Result<Comment> para consistência
    suspend fun addComment(musicId: String, content: String, userName: String): Result<Comment> {
        return handleApiCall {
            val comment = Comment(
                content = content,
                userName = userName
            )
            apiService.addComment(musicId, comment)
        }
    }

    suspend fun getCommentsByMusicId(musicId: String): Result<List<Comment>> {
        return handleApiCall { apiService.getCommentsByMusicId(musicId) }
    }
    suspend fun removeComment(musicId: String, commentId: String): Result<Music> {
        return handleApiCall {
            apiService.removeComment(musicId, commentId)
        }
    }
}