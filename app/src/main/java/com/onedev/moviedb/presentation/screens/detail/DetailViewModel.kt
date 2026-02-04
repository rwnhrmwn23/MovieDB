package com.onedev.moviedb.presentation.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onedev.moviedb.core.utils.Resource
import com.onedev.moviedb.domain.model.MovieDetail
import com.onedev.moviedb.domain.model.Review
import com.onedev.moviedb.domain.model.Video
import com.onedev.moviedb.domain.usecase.GetMovieDetailUseCase
import com.onedev.moviedb.domain.usecase.GetMovieReviewsUseCase
import com.onedev.moviedb.domain.usecase.GetMovieVideosUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailViewModel(
    private val getMovieDetailUseCase: GetMovieDetailUseCase,
    private val getMovieReviewsUseCase: GetMovieReviewsUseCase,
    private val getMovieVideosUseCase: GetMovieVideosUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    fun loadMovieDetail(movieId: Int) {
        viewModelScope.launch {
            _uiState.value = DetailUiState.Loading
            
            val detailDeferred = async { getMovieDetailUseCase(movieId) }
            val reviewsDeferred = async { getMovieReviewsUseCase(movieId) }
            val videosDeferred = async { getMovieVideosUseCase(movieId) }

            val detailResult = detailDeferred.await()
            val reviewsResult = reviewsDeferred.await()
            val videosResult = videosDeferred.await()

            if (detailResult is Resource.Success) {
                _uiState.value = DetailUiState.Success(
                    movie = detailResult.data!!,
                    reviews = reviewsResult.data ?: emptyList(),
                    videos = videosResult.data ?: emptyList()
                )
            } else if (detailResult is Resource.Error) {
                _uiState.value = DetailUiState.Error(detailResult.message ?: "Unknown error")
            }
        }
    }
}

sealed interface DetailUiState {
    data object Loading : DetailUiState
    data class Success(
        val movie: MovieDetail,
        val reviews: List<Review>,
        val videos: List<Video>
    ) : DetailUiState
    data class Error(val message: String) : DetailUiState
}
