package com.onedev.moviedb.core.di

import com.onedev.moviedb.core.network.KtorClient
import com.onedev.moviedb.data.remote.api.MovieApi
import com.onedev.moviedb.data.remote.datasource.MovieRemoteDataSource
import com.onedev.moviedb.data.repository.MovieRepositoryImpl
import com.onedev.moviedb.domain.repository.MovieRepository
import org.koin.dsl.module

val networkModule = module {
    single { KtorClient.create() }
    single { MovieApi(get()) }
    single { MovieRemoteDataSource(get()) }
}

val repositoryModule = module {
    single<MovieRepository> { MovieRepositoryImpl(get()) }
}
