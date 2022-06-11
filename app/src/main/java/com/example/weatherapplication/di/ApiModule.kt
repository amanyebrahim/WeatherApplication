package com.example.weatherapplication.di

import com.example.weatherapplication.model.api.ApiRepo
import com.example.weatherapplication.model.api.ApiRepository
import com.example.weatherapplication.model.api.ApiService
import com.haroldadmin.cnradapter.NetworkResponseAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.example.weatherapplication.model.ModelRepository.Companion.BASE_URL
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.HttpsURLConnection
import kotlin.time.ExperimentalTime

/**
 * ApiModule
 *
 */
@OptIn(KoinApiExtension::class)
@ExperimentalTime
val apiModule = module {
    // Provide custom instance of [HttpLoggingInterceptor] to be attached to Retrofit for logging network calls.
    single {
        HttpLoggingInterceptor().apply {
            level =  HttpLoggingInterceptor.Level.BODY
        }
    }

    // Provide [Cache] to be used in caching network calls.
    single {
        val cacheDirectory = File(androidContext().cacheDir, "WeatherResponses")
        val cacheSize = (5 * 1024 * 1024).toLong() // 5 MB
        Cache(cacheDirectory, cacheSize)
    }

    // Provide custom instance of [OkHttpClient].
    single {
        val httpLoggingInterceptor: HttpLoggingInterceptor by inject()
        val cache: Cache by inject()

        val builder = OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(90, TimeUnit.SECONDS)
            .hostnameVerifier(HostnameVerifier { hostname, session ->
                val hv = HttpsURLConnection.getDefaultHostnameVerifier()

                return@HostnameVerifier when (hostname) {
                    BASE_URL
                        .replace("https://", "")
                        .replace("http://", "") -> true

                    else -> hv.verify(hostname, session)
                }
            })

        builder
            .cache(cache)
            .addInterceptor(httpLoggingInterceptor)

        builder.build()
    }

    // Provide instance of [NetworkResponseAdapterFactory].
    single {
        NetworkResponseAdapterFactory()
    }

    // Provide instance of [MoshiConverterFactory].
    single {
        MoshiConverterFactory.create()
    }

    // Provide instance of [ApiService].
    single {
        val client: OkHttpClient by inject()
        val networkResponseAdapterFactory: NetworkResponseAdapterFactory by inject()
        val moshiConverterFactory: MoshiConverterFactory by inject()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addCallAdapterFactory(networkResponseAdapterFactory)
            .addConverterFactory(moshiConverterFactory)
            .build()
            .create(ApiService::class.java)
    }

    // Provide instance of [ApiRepo].

    single<ApiRepo> {
        ApiRepository(get())
    }

    // Provide instance of [Moshi] with date adapter.
    single {
        Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    }
}