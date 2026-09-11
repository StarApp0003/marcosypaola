package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.R

@Composable
fun MediaImage(
    mediaUri: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    when {
        mediaUri == "res:img_couple_hero" -> {
            Image(
                painter = painterResource(id = R.drawable.img_couple_hero),
                contentDescription = contentDescription,
                modifier = modifier,
                contentScale = contentScale
            )
        }
        mediaUri == "res:img_memory_date" -> {
            Image(
                painter = painterResource(id = R.drawable.img_memory_date),
                contentDescription = contentDescription,
                modifier = modifier,
                contentScale = contentScale
            )
        }
        mediaUri == "res:img_love_letter" -> {
            Image(
                painter = painterResource(id = R.drawable.img_love_letter),
                contentDescription = contentDescription,
                modifier = modifier,
                contentScale = contentScale
            )
        }
        mediaUri == "res:img_couple_icon" -> {
            Image(
                painter = painterResource(id = R.drawable.img_couple_icon),
                contentDescription = contentDescription,
                modifier = modifier,
                contentScale = contentScale
            )
        }
        mediaUri.isNotBlank() -> {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(mediaUri)
                    .crossfade(true)
                    .build(),
                contentDescription = contentDescription,
                modifier = modifier,
                contentScale = contentScale
            )
        }
        else -> {
            Box(
                modifier = modifier,
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxSize(0.4f)
                )
            }
        }
    }
}
