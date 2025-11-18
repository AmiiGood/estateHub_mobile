package com.oscar.estatehubcompose.analisis.domain

import android.util.Log
import com.google.gson.Gson
import com.oscar.estatehubcompose.analisis.Models.GeocodificadorInfo
import com.oscar.estatehubcompose.analisis.data.AnalisisRepository
import com.oscar.estatehubcompose.analisis.data.network.response.ParsedGeminiResponse
import com.oscar.estatehubcompose.analisis.helpers.cleanGeminiResponse
import javax.inject.Inject

class GeminiUseCase @Inject constructor(private val analisisRepository: AnalisisRepository) {

    suspend operator fun invoke(colonia: String, codigoPostal: String, ciudad: String, estado: String, geocodificadorInfo: GeocodificadorInfo?): ParsedGeminiResponse?{
        var response = analisisRepository.geminiAnalizar(colonia, codigoPostal, ciudad, estado, geocodificadorInfo);

        val gson = Gson();
        if(response == null){
            return null
        }

        val cleanResponse = cleanGeminiResponse(response.candidates[0].content.parts[0].text);
        Log.i("OSCAR", cleanResponse);

        val parsedGeminiResponse = gson.fromJson( cleanResponse,
            ParsedGeminiResponse::class.java)


        return parsedGeminiResponse;

    }

}