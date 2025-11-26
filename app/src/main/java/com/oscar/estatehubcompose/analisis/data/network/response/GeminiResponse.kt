package com.oscar.estatehubcompose.analisis.data.network.response

import com.google.gson.annotations.SerializedName

data class GeminiResponse(
    @SerializedName("candidates")
    val candidates: List<Candidate>? = null,

    @SerializedName("usageMetadata")
    val usageMetadata: UsageMetadata? = null,

    @SerializedName("modelVersion")
    val modelVersion: String? = null,

    @SerializedName("responseId")
    val responseId: String? = null
)

data class Candidate(
    @SerializedName("content")
    val content: Content? = null,

    @SerializedName("finishReason")
    val finishReason: String? = null,

    @SerializedName("index")
    val index: Int? = null
)

data class Content(
    @SerializedName("parts")
    val parts: List<Part>? = null,

    @SerializedName("role")
    val role: String? = null
)

data class Part(
    @SerializedName("text")
    val text: String = ""
)

data class UsageMetadata(
    @SerializedName("promptTokenCount")
    val promptTokenCount: Int? = null,

    @SerializedName("candidatesTokenCount")
    val candidatesTokenCount: Int? = null,

    @SerializedName("totalTokenCount")
    val totalTokenCount: Int? = null,

    @SerializedName("promptTokensDetails")
    val promptTokensDetails: List<TokenDetail>? = null,

    @SerializedName("thoughtsTokenCount")
    val thoughtsTokenCount: Int? = null
)

data class TokenDetail(
    @SerializedName("modality")
    val modality: String? = null,

    @SerializedName("tokenCount")
    val tokenCount: Int? = null
)