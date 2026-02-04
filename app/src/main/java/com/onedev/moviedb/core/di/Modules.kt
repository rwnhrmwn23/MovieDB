package com.onedev.moviedb.core.di

import com.onedev.moviedb.data.repository.FakeMovieRepository
import com.onedev.moviedb.domain.repository.MovieRepository
import com.onedev.moviedb.domain.usecase.GetGenresUseCase
import com.onedev.moviedb.domain.usecase.GetMovieDetailUseCase
import com.onedev.moviedb.domain.usecase.GetMovieReviewsUseCase
import com.onedev.moviedb.domain.usecase.GetMovieVideosUseCase
import com.onedev.moviedb.domain.usecase.GetMoviesByGenreUseCase
import com.onedev.moviedb.domain.usecase.SearchMoviesUseCase
import com.onedev.moviedb.presentation.screens.detail.DetailViewModel
import com.onedev.moviedb.presentation.screens.movie.MovieViewModel
import com.onedev.moviedb.presentation.screens.search.SearchViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    includes(networkModule, repositoryModule)
    
    // UseCases
    factory { GetGenresUseCase(get()) }
    factory { GetMoviesByGenreUseCase(get()) }
    factory { GetMovieDetailUseCase(get()) }
    factory { GetMovieReviewsUseCase(get()) }
    factory { GetMovieVideosUseCase(get()) }
    factory { SearchMoviesUseCase(get()) }
    
    // ViewModels
    viewModel { MovieViewModel(get(), get()) }
    viewModel { SearchViewModel(get()) }
    viewModel { DetailViewModel(get(), get(), get()) }
}
