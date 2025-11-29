package com.oscar.estatehubcompose.analisis.domain

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.oscar.estatehubcompose.analisis.Models.GeocodificadorInfo
import com.oscar.estatehubcompose.analisis.data.AnalisisRepository
import com.oscar.estatehubcompose.analisis.data.network.response.ParsedGeminiResponse
import com.oscar.estatehubcompose.analisis.helpers.cleanGeminiResponse
import javax.inject.Inject

class GeminiUseCase @Inject constructor(private val analisisRepository: AnalisisRepository) {

    suspend operator fun invoke(
        colonia: String,
        codigoPostal: String,
        ciudad: String,
        estado: String,
        geocodificadorInfo: GeocodificadorInfo?
    ): ParsedGeminiResponse? {

        val response = analisisRepository.geminiAnalizar(colonia, codigoPostal, ciudad, estado, geocodificadorInfo)

        if (response == null) {
            Log.e("OSCAR", "Response es null")
            return null
        }

        if (response.candidates.isNullOrEmpty()) {
            Log.e("OSCAR", "Candidates está vacío o null")
            Log.e("OSCAR", "Response completo: $response")
            return null
        }

        val firstCandidate = response.candidates[0]
        if (firstCandidate.content?.parts.isNullOrEmpty()) {
            Log.e("OSCAR", "Content o parts está vacío")
            Log.e("OSCAR", "Candidate: $firstCandidate")
            return null
        }

        val rawText = firstCandidate.content.parts[0].text
        if (rawText.isBlank()) {
            Log.e("OSCAR", "Texto está vacío")
            return null
        }

        Log.i("OSCAR", "Texto raw recibido: ${rawText.take(200)}...")

        var cleanResponse = cleanGeminiResponse(rawText)

        cleanResponse = processPriceArrays(cleanResponse)

        Log.i("OSCAR", "Texto limpio y procesado: ${cleanResponse.take(500)}...")

        return try {
            val gson = Gson()
            val parsedResponse = gson.fromJson(cleanResponse, ParsedGeminiResponse::class.java)
            Log.i("OSCAR", "JSON parseado exitosamente")
            parsedResponse
        } catch (e: JsonSyntaxException) {
            Log.e("OSCAR", "Error parseando JSON: ${e.message}")
            Log.e("OSCAR", "JSON completo:\n$cleanResponse")
            e.printStackTrace()
            null
        } catch (e: Exception) {
            Log.e("OSCAR", "Error inesperado: ${e.message}")
            e.printStackTrace()
            null
        }
    }

    private fun processPriceArrays(json: String): String {
        var processed = json

        val priceFields = listOf("casa", "local_comercial", "departamento")

        priceFields.forEach { fieldName ->
            val pattern = Regex(""""$fieldName":\s*\[([^\]]+)\]""")

            pattern.findAll(processed).forEach { match ->
                val arrayContent = match.groupValues[1]

                val numbers = Regex("""\d+""").findAll(arrayContent)
                    .mapNotNull { it.value.toIntOrNull() }
                    .toList()

                if (numbers.isNotEmpty()) {
                    val average = numbers.average().toInt()

                    val originalMatch = match.value
                    val replacement = """"$fieldName": $average"""

                    processed = processed.replace(originalMatch, replacement)

                    Log.d("OSCAR", "🔄 $fieldName: array $numbers → promedio $average")
                }
            }
        }

        processed = processed.replace(Regex(""""(\w+)":\s*"\[([^\]]+)\]"""")) { match ->
            val fieldName = match.groupValues[1]
            val content = match.groupValues[2]
            Log.d("OSCAR", "🧹 Limpiando corchetes en: $fieldName")
            """"$fieldName": "$content""""
        }

        return processed
    }
}