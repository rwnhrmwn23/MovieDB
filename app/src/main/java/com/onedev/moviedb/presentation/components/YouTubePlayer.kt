package com.onedev.moviedb.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import io.github.ilyapavlovskii.multiplatform.youtubeplayer.SimpleYouTubePlayerOptionsBuilder
import io.github.ilyapavlovskii.multiplatform.youtubeplayer.YouTubePlayer
import io.github.ilyapavlovskii.multiplatform.youtubeplayer.YouTubePlayerHostState
import io.github.ilyapavlovskii.multiplatform.youtubeplayer.YouTubeVideoId

@Composable
fun YouTubePlayer(
    videoId: String,
    modifier: Modifier = Modifier
) {
    val hostState = remember { YouTubePlayerHostState() }

    LaunchedEffect(videoId) {
        hostState.loadVideo(YouTubeVideoId(videoId))
    }

    YouTubePlayer(
        modifier = modifier.fillMaxWidth(),
        hostState = hostState,
        options = SimpleYouTubePlayerOptionsBuilder.builder {
            autoplay(false)
            controls(true)
            rel(false)
        }
    )
}
