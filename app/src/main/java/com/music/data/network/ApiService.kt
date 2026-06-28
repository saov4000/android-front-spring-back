// ApiService.kt
package com.music.data.network

import com.music.data.model.Music
import com.music.data.model.Comment
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("api/musics")
    suspend fun getMusics(): Response<List<Music>>

    @GET("api/musics/{id}")
    suspend fun getMusicById(@Path("id") id: String): Response<Music>

    @POST("api/musics")
    suspend fun createMusic(@Body music: Music): Response<Music>

    @PUT("api/musics/{id}")
    suspend fun updateMusic(@Path("id") id:String, @Body music: Music): Response<Music>

    @DELETE("api/musics/{id}")
    suspend fun deleteMusic(@Path("id") id: String): Response<Unit>

    @POST("api/musics/{id}/comments")
    suspend fun addComment(@Path("id") musicId: String,@Body comment: Comment): Response<Comment>

    @GET("api/musics/{id}/comments")
    suspend fun getCommentsByMusicId(@Path("id") musicId: String): Response<List<Comment>>
    @DELETE("api/musics/{musicId}/comments/{commentId}")
    suspend fun removeComment(
        @Path("musicId") musicId: String,
        @Path("commentId") commentId: String
    ): Response<Music>
}