package com.onedev.moviedb.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.onedev.moviedb.data.mapper.MovieMapper
import com.onedev.moviedb.data.remote.datasource.MovieRemoteDataSource
import com.onedev.moviedb.domain.model.Movie
import io.ktor.client.plugins.ClientRequestException

class MoviePagingSource(
    private val remoteDataSource: MovieRemoteDataSource,
    private val genreIds: List<Int>,
    private val query: String? = null
) : PagingSource<Int, Movie>() {

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val page = params.key ?: 1
            
            // Format genreIds string with pipe separator | E.g "12|16"
            // If query is present, we use search endpoint, otherwise discover by genre
            val response = if (!query.isNullOrEmpty()) {
                remoteDataSource.searchMovies(query, page)
            } else {
                 val genreIdsParam = if (genreIds.isNotEmpty()) {
                    genreIds.joinToString("|")
                } else {
                    "" // Should probably handle case where no genre selected? API might allow empty
                }
                
                // If genreIds is empty and no query, maybe discover popular? 
                // Detailed logic: user flow shows genres list, selecting one or more triggers this.
                // Assuming at least one genre is passed or handled by empty check logic in ViewModel/UseCase
                remoteDataSource.getMoviesByGenre(genreIdsParam, page)
            }

            val movies = response.results.map { MovieMapper.mapMovieDtoToDomain(it) }
            
            LoadResult.Page(
                data = movies,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (page < response.totalPages) page + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
