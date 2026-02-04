package com.onedev.moviedb.core.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import com.onedev.moviedb.BuildConfig

object KtorClient {
    fun create(): HttpClient {
        return HttpClient(OkHttp) {
            defaultRequest {
                url(ApiConstants.BASE_URL)
                header(HttpHeaders.Authorization, "Bearer ${BuildConfig.TMDB_API_KEY}")
                header(HttpHeaders.Accept, "application/json")
            }
            
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                })
            }
            
            install(Logging) {
                logger = Logger.ANDROID
                level = LogLevel.BODY
            }
        }
    }
}
