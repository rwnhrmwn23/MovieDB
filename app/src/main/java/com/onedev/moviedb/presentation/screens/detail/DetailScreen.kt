package com.onedev.moviedb.presentation.screens.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.onedev.moviedb.core.network.ApiConstants
import com.onedev.moviedb.presentation.components.GenreChip
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    movieId: Int,
    onNavigateBack: () -> Unit,
    onNavigateToVideo: (String, String, String) -> Unit,
    viewModel: DetailViewModel = koinViewModel()
) {
    LaunchedEffect(movieId) {
        viewModel.loadMovieDetail(movieId)
    }

    val uiState by viewModel.uiState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current

    // Parallax & Scroll State
    val listState = rememberLazyListState()
    val showAppBarTitle by remember {
        derivedStateOf {
            // Show title if we've scrolled past the header (approx 250.dp)
            listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 600
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (uiState is DetailUiState.Success && showAppBarTitle) {
                        Text(
                            text = (uiState as DetailUiState.Success).movie.title,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier
                            .background(
                                color = if (showAppBarTitle) Color.Transparent else Color.Black.copy(
                                    alpha = 0.5f
                                ),
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = if (showAppBarTitle) MaterialTheme.colorScheme.onSurface else Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (showAppBarTitle) MaterialTheme.colorScheme.background else Color.Transparent,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
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
                        state = listState,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(325.dp)
                            ) {
                                // Parallax Backdrop
                                AsyncImage(
                                    model = if (movie.backdropPath != null) ApiConstants.IMAGE_BASE_URL_ORIGINAL + movie.backdropPath else null,
                                    contentDescription = movie.title,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(250.dp)
                                        .graphicsLayer {
                                            translationY =
                                                listState.firstVisibleItemScrollOffset / 2f
                                            alpha =
                                                1f - (listState.firstVisibleItemScrollOffset / 1000f).coerceIn(
                                                    0f,
                                                    1f
                                                )
                                        },
                                    contentScale = ContentScale.Crop
                                )

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(100.dp)
                                        .align(Alignment.BottomCenter)
                                )

                                Row(
                                    modifier = Modifier
                                        .align(Alignment.BottomStart)
                                        .padding(horizontal = 16.dp),
                                    verticalAlignment = Alignment.Bottom
                                ) {
                                    // Poster Card
                                    Card(
                                        shape = RoundedCornerShape(12.dp),
                                        elevation = CardDefaults.cardElevation(8.dp),
                                        modifier = Modifier
                                            .width(120.dp)
                                            .aspectRatio(0.67f)
                                    ) {
                                        AsyncImage(
                                            model = if (movie.posterPath != null) ApiConstants.IMAGE_BASE_URL + movie.posterPath else null,
                                            contentDescription = "Poster",
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = ContentScale.Crop
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(16.dp))

                                    // Title next to poster
                                    Column(
                                        modifier = Modifier
                                            .padding(bottom = 12.dp)
                                    ) {
                                        Text(
                                            text = movie.title,
                                            style = MaterialTheme.typography.headlineSmall,
                                            color = MaterialTheme.colorScheme.onBackground,
                                            maxLines = 3,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "${movie.runtime} min • ★ ${movie.voteAverage}",
                                            style = MaterialTheme.typography.labelMedium,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            }
                        }

                        // Info & Content
                        item {
                            Column(modifier = Modifier.padding(16.dp)) {
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
                                    text = movie.tagline ?: "",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                    color = MaterialTheme.colorScheme.secondary
                                )

                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = movie.overview,
                                    style = MaterialTheme.typography.bodyLarge
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
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = review.author,
                                            style = MaterialTheme.typography.titleMedium,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        if (review.username.isNotEmpty()) {
                                            Text(
                                                text = " (${review.username})",
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                                modifier = Modifier.padding(start = 4.dp)
                                            )
                                        }
                                    }

                                    if (review.rating != null) {
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Filled.Star,
                                                contentDescription = "Rating",
                                                tint = Color(0xFFFFD700), // Gold/Yellow
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = "${review.rating}",
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = review.content,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    androidx.compose.material3.HorizontalDivider()
                                }
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

                            val trailers = state.videos
                                .filter { it.site == "YouTube" }
                            // .take(3)

                            items(trailers) { video ->
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 16.dp)
                                        .clickable {
                                            onNavigateToVideo(video.key, video.name, video.type)
                                        }
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp)
                                            .aspectRatio(16f / 9f)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(Color.Black),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        // YouTube Thumbnail
                                        AsyncImage(
                                            model = "https://img.youtube.com/vi/${video.key}/hqdefault.jpg",
                                            contentDescription = video.name,
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = ContentScale.Crop
                                        )

                                        // Dark overlay
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(Color.Black.copy(alpha = 0.3f))
                                        )

                                        // Play Icon
                                        Icon(
                                            imageVector = Icons.Rounded.PlayArrow,
                                            contentDescription = "Play Trailer",
                                            tint = Color.White,
                                            modifier = Modifier.size(64.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = video.name,
                                        style = MaterialTheme.typography.titleMedium,
                                        modifier = Modifier.padding(horizontal = 16.dp),
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
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
