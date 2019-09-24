package com.example.imagurtask.di

import com.example.imagurtask.AUTHORIZATION_HEADER_KEY
import com.example.imagurtask.BuildConfig
import com.example.imagurtask.remote.RemoteDataSource
import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

fun remoteDataSourceModule(): Module {
    return module {
        this.factory { createHttpLoggingInterceptor() }
        factory { createAuthHeaderInterceptor() }
        factory { createOkHttpClient(get(), get()) }
        factory { createGsonConverter() }
        factory { createRxJava2CallAdapter() }
        single {
            createWebService<RemoteDataSource>(
                get(),
                get(),
                get(),
                BuildConfig.baseUrl
            )
        }
    }
}

fun createHttpLoggingInterceptor(): HttpLoggingInterceptor {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    return httpLoggingInterceptor
}

fun createAuthHeaderInterceptor(): Interceptor {
    return Interceptor {
        val original = it.request()
        val request = original.newBuilder()
        request.addHeader(AUTHORIZATION_HEADER_KEY, "Client-ID ${BuildConfig.clientIdKey}")
        request.method(original.method(), original.body())
        it.proceed(request.build())
    }
}

fun createOkHttpClient(
    httpLoggingInterceptor: HttpLoggingInterceptor,
    interceptor: Interceptor
): OkHttpClient {
    val httpClient = OkHttpClient.Builder()
    httpClient.connectTimeout(60L, TimeUnit.SECONDS)
    httpClient.readTimeout(60L, TimeUnit.SECONDS)
    httpClient.addInterceptor(httpLoggingInterceptor)
    httpClient.addInterceptor(interceptor)
    if (BuildConfig.DEBUG)
        httpClient.addNetworkInterceptor(StethoInterceptor())
    return httpClient.build()
}

fun createGsonConverter(): GsonConverterFactory {
    return GsonConverterFactory.create()
}

fun createRxJava2CallAdapter(): RxJava2CallAdapterFactory {
    return RxJava2CallAdapterFactory.create()
}

inline fun <reified T> createWebService(
    okHttpClient: OkHttpClient,
    gsonConverterFactory: GsonConverterFactory,
    rxJava2CallAdapter: RxJava2CallAdapterFactory,
    url: String
): T {
    return Retrofit.Builder()
        .baseUrl(url)
        .client(okHttpClient)
        .addConverterFactory(gsonConverterFactory)
        .addCallAdapterFactory(rxJava2CallAdapter)
        .build().create(T::class.java)
}