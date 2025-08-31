package com.islam97.android.apps.movie.core.di.modules

import com.google.gson.Gson
import com.islam97.android.apps.movie.BuildConfig
import com.islam97.android.apps.movie.core.di.qualifiers.ApiKey
import com.islam97.android.apps.movie.core.di.qualifiers.BaseUrl
import com.islam97.android.apps.movie.data.remote.MovieApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Singleton
    @Provides
    fun provideRetrofit(
        @BaseUrl baseUrl: String,
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl).client(okHttpClient)
            .addConverterFactory(gsonConverterFactory).build()
    }

    @Singleton
    @Provides
    fun provideHttpClient(@ApiKey apiKey: String): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(Interceptor {
            val request: Request = it.request()
            val url = request.url.newBuilder().addQueryParameter("api_key", apiKey).build()
            val newRequest = request.newBuilder().url(url).build()
            it.proceed(newRequest)
        }).build()
    }

    @Singleton
    @Provides
    fun provideGson(): Gson {
        return Gson()
    }

    @Singleton
    @Provides
    fun provideGsonConverterFactory(gson: Gson): GsonConverterFactory {
        return GsonConverterFactory.create(gson)
    }

    @Singleton
    @Provides
    @BaseUrl
    fun provideBaseUrl(): String {
        return BuildConfig.BASE_URL
    }

    @Singleton
    @Provides
    @ApiKey
    fun provideApiKey(): String {
        return BuildConfig.API_KEY
    }

    @Singleton
    @Provides
    fun provideMovieApi(retrofit: Retrofit): MovieApi {
        return retrofit.create(MovieApi::class.java)
    }
}