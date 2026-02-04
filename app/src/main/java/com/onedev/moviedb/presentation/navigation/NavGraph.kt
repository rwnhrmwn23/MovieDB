package com.onedev.moviedb.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.onedev.moviedb.presentation.screens.detail.DetailScreen
import com.onedev.moviedb.presentation.screens.movie.MovieScreen
import com.onedev.moviedb.presentation.screens.search.SearchScreen
import com.onedev.moviedb.presentation.screens.video.VideoPlayerScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: Screen = Screen.MovieList
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable<Screen.MovieList> {
            MovieScreen(
                onNavigateToSearch = { navController.navigate(Screen.Search) },
                onNavigateToDetail = { movieId -> navController.navigate(Screen.Detail(movieId)) }
            )
        }
        
        composable<Screen.Search> {
            SearchScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToDetail = { movieId -> navController.navigate(Screen.Detail(movieId)) }
            )
        }
        
        composable<Screen.Detail> { backStackEntry ->
            val detail: Screen.Detail = backStackEntry.toRoute()
            DetailScreen(
                movieId = detail.movieId,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToVideo = { videoId, videoTitle, videoType ->
                    navController.navigate(Screen.VideoPlayer(videoId, videoTitle, videoType))
                }
            )
        }
        
        composable<Screen.VideoPlayer> { backStackEntry ->
            val video: Screen.VideoPlayer = backStackEntry.toRoute()
            VideoPlayerScreen(
                videoId = video.videoId,
                videoTitle = video.videoTitle,
                videoType = video.videoType,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
