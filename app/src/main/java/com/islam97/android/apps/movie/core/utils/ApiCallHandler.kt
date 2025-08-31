package com.islam97.android.apps.movie.core.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.islam97.android.apps.movie.R
import com.islam97.android.apps.movie.data.dto.ErrorResponseDto
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiCallHandler
@Inject constructor(@ApplicationContext private val context: Context, private val gson: Gson) {
    suspend fun <T, S> callApi(apiCall: suspend () -> T, map: T.() -> S?): Result<S> {
        return try {
            Result.Success(apiCall().map())
        } catch (e: HttpException) {
            e.response()?.errorBody()?.string()?.let {
                try {
                    val errorResponse = gson.fromJson(it, ErrorResponseDto::class.java)
                    Result.Error(
                        errorResponse.statusMessage ?: context.getString(
                            R.string.error_network,
                            e.code()
                        )
                    )
                } catch (t: JsonSyntaxException) {
                    Result.Error(context.getString(R.string.error_network, e.code()))
                }
            } ?: Result.Error(context.getString(R.string.error_network, e.code()))
        } catch (e: Throwable) {
            Result.Error(
                e.message ?: context.getString(R.string.error_something_went_wrong)
            )
        }
    }
}