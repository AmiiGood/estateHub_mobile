package com.oscar.estatehubcompose.analisis.helpers

fun cleanGeminiResponse(rawText: String): String {
    var cleaned = rawText
        .replace("```", "")
        .replace("```", "")
        .trim()

    val startIndex = cleaned.indexOf('{')
    val endIndex = cleaned.lastIndexOf('}')

    if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
        cleaned = cleaned.substring(startIndex, endIndex + 1)
    }

    return cleaned
}
