package com.example.moviewatchlist.model.remote

import com.example.moviewatchlist.BuildConfig
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

// One shared Retrofit client for the whole app, created the first time it is used.
object RetrofitInstance {

    private const val BASE_URL = "https://api.themoviedb.org/3/"

    // ignoreUnknownKeys: TMDB sends many fields we do not use.
    // coerceInputValues: a null or unknown value falls back to the field's default instead of throwing.
    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    // Adds ?api_key=... to every request, so TmdbApi does not repeat it on each endpoint.
    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val url = chain.request().url.newBuilder()
                .addQueryParameter("api_key", BuildConfig.TMDB_API_KEY)
                .build()
            chain.proceed(chain.request().newBuilder().url(url).build())
        }
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                // BASIC logs the URL and status code in Logcat; it hides the response body.
                level = if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BASIC
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
            }
        )
        .build()

    val api: TmdbApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(TmdbApi::class.java)
    }
}
