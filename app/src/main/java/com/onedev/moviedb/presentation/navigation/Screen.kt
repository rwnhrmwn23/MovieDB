package com.onedev.moviedb.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable
    data object MovieList : Screen

    @Serializable
    data object Search : Screen

    @Serializable
    data class Detail(val movieId: Int) : Screen
}
