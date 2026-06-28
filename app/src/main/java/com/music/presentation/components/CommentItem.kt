package com.music.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.music.data.model.Comment

@Composable
fun CommentItem(
    comment: Comment,
    onDelete: (String) -> Unit,
    isOwner: Boolean = true
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFd3d3d3)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = comment.content ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )
                Text(
                    text = "Por: ${comment.userName}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Black
                )

                if (!comment.createdAt.isNullOrEmpty()) {
                    Text(
                        text = "Data: ${comment.createdAt}",
                        color = Color.Black,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            if (isOwner) {
                Row {
                    IconButton(
                        onClick = { comment.id?.let { onDelete(it) } },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Deletar comentário",
                            tint = Color(0xFFE74C3C)
                        )
                    }
                }
            }
        }
    }
}