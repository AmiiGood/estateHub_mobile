package com.oscar.estatehubcompose.analisis.data.network.request

import com.google.gson.annotations.SerializedName

data class GeminiRequest(
    @SerializedName("contents")
    val contents: List<Content>,

    @SerializedName("generationConfig")
    val generationConfig: GenerationConfig? = null
)

data class GenerationConfig(
    @SerializedName("temperature")
    val temperature: Double = 0.7,

    @SerializedName("maxOutputTokens")
    val maxOutputTokens: Int = 1024,

    @SerializedName("topP")
    val topP: Double = 0.95,

    @SerializedName("topK")
    val topK: Int = 40
)

data class Content(
    @SerializedName("parts")
    val parts: List<Part>
)

data class Part(
    @SerializedName("text")
    val text: String
)