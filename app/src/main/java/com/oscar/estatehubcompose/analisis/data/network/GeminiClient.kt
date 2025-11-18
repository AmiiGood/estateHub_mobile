package com.oscar.estatehubcompose.analisis.data.network

import com.oscar.estatehubcompose.analisis.data.network.request.GeminiRequest
import com.oscar.estatehubcompose.analisis.data.network.response.GeminiResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface GeminiClient {
    @POST("v1beta/models/gemini-2.5-pro:generateContent")
    suspend fun generateContent(
        @Header("x-goog-api-key") apiKey: String,
        @Body() request: GeminiRequest
    ): GeminiResponse
}