package com.islam97.android.apps.movie.data.dto

import com.google.gson.annotations.SerializedName

data class ErrorResponseDto(
    @SerializedName("status_code") val statusCode: Int?,
    @SerializedName("status_message") val statusMessage: String?,
    @SerializedName("success") val success: Boolean?,
)