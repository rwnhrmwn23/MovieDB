package com.onedev.moviedb.data.repository

import androidx.paging.PagingData
import com.onedev.moviedb.core.utils.Resource
import com.onedev.moviedb.domain.model.Genre
import com.onedev.moviedb.domain.model.Movie
import com.onedev.moviedb.domain.model.MovieDetail
import com.onedev.moviedb.domain.model.Review
import com.onedev.moviedb.domain.model.Video
import com.onedev.moviedb.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeMovieRepository : MovieRepository {
    override suspend fun getGenres(): Resource<List<Genre>> {
        return Resource.Success(
            listOf(
                Genre(1, "Action"),
                Genre(2, "Adventure"),
                Genre(3, "Animation"),
                Genre(4, "Comedy"),
                Genre(5, "Crime"),
                Genre(6, "Documentary"),
                Genre(7, "Drama"),
                Genre(8, "Family"),
                Genre(9, "Fantasy"),
                Genre(10, "History"),
                Genre(11, "Horror")
            )
        )
    }

    override fun getMoviesByGenre(genreIds: List<Int>): Flow<PagingData<Movie>> {
        val movies = List(20) { index ->
            Movie(
                id = index,
                title = "Movie Title $index",
                overview = "This is a dummy overview for movie $index. It has some text to show.",
                posterPath = null,
                backdropPath = null,
                voteAverage = 7.5,
                releaseDate = "2026-01-01",
                genreIds = genreIds
            )
        }
        return flowOf(PagingData.from(movies))
    }

    override fun searchMovies(query: String): Flow<PagingData<Movie>> {
        val movies = List(5) { index ->
            Movie(
                id = index + 100,
                title = "Result for '$query' $index",
                overview = "Search result description.",
                posterPath = null,
                backdropPath = null,
                voteAverage = 8.0,
                releaseDate = "2026-02-01",
                genreIds = listOf(1, 2)
            )
        }
        return flowOf(PagingData.from(movies))
    }

    override suspend fun getMovieDetail(movieId: Int): Resource<MovieDetail> {
        return Resource.Success(
            MovieDetail(
                id = movieId,
                title = "Detailed Movie $movieId",
                overview = "This is a detailed overview for the movie. It contains more information about the plot, characters, and setting.",
                posterPath = null,
                backdropPath = null,
                voteAverage = 8.5,
                releaseDate = "2026-01-01",
                genres = listOf(Genre(1, "Action"), Genre(2, "Sci-Fi")),
                runtime = 120,
                tagline = "The best movie ever."
            )
        )
    }

    override suspend fun getMovieReviews(movieId: Int): Resource<List<Review>> {
        return Resource.Success(
            List(5) { index ->
                Review(
                    id = "review_$index",
                    author = "User $index",
                    content = "This movie was great! I really enjoyed the scenes and the acting.",
                    createdAt = "2026-01-1$index",
                    avatarPath = null
                )
            }
        )
    }

    override suspend fun getMovieVideos(movieId: Int): Resource<List<Video>> {
        return Resource.Success(
            listOf(
                Video("v1", "dQw4w9WgXcQ", "Official Trailer", "YouTube", "Trailer"),
                Video("v2", "M7lc1UVf-VE", "Teaser", "YouTube", "Teaser")
            )
        )
    }
}
