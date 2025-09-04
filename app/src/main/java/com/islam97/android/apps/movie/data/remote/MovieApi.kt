package com.islam97.android.apps.movie.data.remote

import com.islam97.android.apps.movie.data.dto.MovieDto
import com.islam97.android.apps.movie.data.dto.MovieListResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {
    @GET("3/discover/movie?include_adult=false&include_video=false")
    suspend fun getMovieList(@Query("page") page: Int): MovieListResponseDto

    @GET("3/movie/{movieId}")
    suspend fun getMovieDetails(@Path("movieId") movieId: Int): MovieDto

    @GET("3/search/movie?include_adult=false")
    suspend fun search(
        @Query("query") searchQuery: String, @Query("page") page: Int
    ): MovieListResponseDto
}