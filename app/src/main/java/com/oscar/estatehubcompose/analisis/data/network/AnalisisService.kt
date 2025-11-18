package com.oscar.estatehubcompose.analisis.data.network

import android.util.Log
import com.oscar.estatehubcompose.analisis.data.network.request.AnalisisRequest
import com.oscar.estatehubcompose.analisis.data.network.request.GeminiRequest
import com.oscar.estatehubcompose.analisis.data.network.request.GeocodificadorRequest
import com.oscar.estatehubcompose.analisis.data.network.response.AnalisisResponse
import com.oscar.estatehubcompose.analisis.data.network.response.GeminiResponse
import com.oscar.estatehubcompose.analisis.data.network.response.GeocodificadorResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Response
import javax.inject.Inject

class AnalisisService @Inject constructor(private val analisisClient: AnalisisClient,
                                          private val geminiClient: GeminiClient) {


    suspend fun analizar(analisisRequest: AnalisisRequest): AnalisisResponse? {
        return withContext(Dispatchers.IO){
            val response = analisisClient.analizar(analisisRequest);
            Log.i("OSCAR", "${response}");
            response.body();
        }
    }


    suspend fun geocodificar(geocodificadorRequest: GeocodificadorRequest): GeocodificadorResponse? {
        return withContext(Dispatchers.IO){
            val response = analisisClient.geocodificar(geocodificadorRequest);
            Log.i("OSCAR", "${response}");
            response.body();
        }
    }

    suspend fun analizarGemini(geminiRequest: GeminiRequest): GeminiResponse? {
        return withContext(Dispatchers.IO){

            try {
                val response = geminiClient.generateContent("",geminiRequest);
                Log.i("OSCAR", response.toString());
                response;
            }catch (e: Exception){
                Log.e("OSCAR", "Error: ${e.message}")
                null
            }

        }
    };
}