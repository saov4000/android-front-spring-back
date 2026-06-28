package com.music.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.music.data.model.Music

@Composable
fun MusicCard(
    music: Music,
    onNavigateToDetail: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onNavigateToDetail(music.id!!) },
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1A2332)
        ),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(
            width = 1.dp,
            color = Color(0xFF2C3E6B)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = music.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.LightGray,
                    fontSize = 22.sp
                )
                Text(
                    text = "Artista: ${music.artist}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.LightGray,
                    fontSize = 20.sp
                )
                Text(
                    text = "Álbum: ${music.album}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF8A94A8),
                    fontSize = 18.sp
                )
                Surface(
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(top = 4.dp),
                    color = Color(0xFF2C3E6B),  // 👈 Fundo da caixa do gênero
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(
                        width = 1.dp,
                        color = Color(0xFF4A6A9A)  // 👈 Borda da caixa
                    )
                ) {
                    Text(
                        text = "Gênero: ${music.genre}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFB0C4DE),  // 👈 Texto claro
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}