package com.islam97.android.apps.movie.data.remote

import com.islam97.android.apps.movie.data.dto.MovieListResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApi {
    @GET("3/discover/movie?include_adult=false&include_video=false")
    suspend fun getMovieList(@Query("page") page: Int): MovieListResponseDto
}