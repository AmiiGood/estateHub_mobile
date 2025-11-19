package com.oscar.estatehubcompose.analisis.data.network.request

import com.google.gson.annotations.SerializedName

data class GeminiRequest(
    @SerializedName("contents")
    val contents: List<Content>
)

data class Content(
    @SerializedName("parts")
    val parts: List<Part>
)

data class Part(
    @SerializedName("text")
    val text: String
)