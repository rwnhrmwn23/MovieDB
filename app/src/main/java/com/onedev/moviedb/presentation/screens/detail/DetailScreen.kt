package com.onedev.moviedb.presentation.screens.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.onedev.moviedb.core.network.ApiConstants
import com.onedev.moviedb.presentation.components.GenreChip
import com.onedev.moviedb.presentation.components.YouTubePlayer
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    movieId: Int,
    onNavigateBack: () -> Unit,
    viewModel: DetailViewModel = koinViewModel()
) {
    LaunchedEffect(movieId) {
        viewModel.loadMovieDetail(movieId)
    }

    val uiState by viewModel.uiState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier
                            .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            when (val state = uiState) {
                is DetailUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is DetailUiState.Error -> {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is DetailUiState.Success -> {
                    val movie = state.movie
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // Poster/Backdrop
                        item {
                            AsyncImage(
                                model = if (movie.backdropPath != null) ApiConstants.IMAGE_BASE_URL_ORIGINAL + movie.backdropPath else null,
                                contentDescription = movie.title,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(1.5f),
                                contentScale = ContentScale.Crop
                            )
                        }

                        // Info
                        item {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = movie.title,
                                    style = MaterialTheme.typography.displaySmall,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    items(movie.genres) { genre ->
                                        GenreChip(
                                            genre = genre,
                                            isSelected = true,
                                            onToggle = {}
                                        )
                                    }
                                }
                                
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = movie.overview,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }

                        // Trailers
                        if (state.videos.isNotEmpty()) {
                            item {
                                Text(
                                    text = "Trailers",
                                    style = MaterialTheme.typography.headlineSmall,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                            items(state.videos.filter { it.site == "YouTube" }) { video ->
                                YouTubePlayer(
                                    videoId = video.key,
                                    lifecycleOwner = lifecycleOwner,
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp, vertical = 8.dp)
                                        .aspectRatio(16f / 9f)
                                )
                            }
                        }

                        // Reviews
                        if (state.reviews.isNotEmpty()) {
                            item {
                                Text(
                                    text = "Reviews",
                                    style = MaterialTheme.typography.headlineSmall,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                            items(state.reviews) { review ->
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = review.author,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = review.content,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    VerticalDivider()
                                }
                            }
                        }
                        
                        // Bottom spacer
                        item { Spacer(modifier = Modifier.height(80.dp)) }
                    }
                }
            }
        }
    }
}
