package com.oscar.estatehubcompose.analisis.data.network

import android.util.Log
import com.oscar.estatehubcompose.BuildConfig
import com.oscar.estatehubcompose.analisis.data.network.request.GeminiRequest
import com.oscar.estatehubcompose.analisis.data.network.request.GeocodificadorRequest
import com.oscar.estatehubcompose.analisis.data.network.response.GeminiResponse
import com.oscar.estatehubcompose.analisis.data.network.response.GeocodificadorResponse
import com.oscar.estatehubcompose.analisis.data.network.response.PropiedadesResponse
import com.oscar.estatehubcompose.helpers.DataStoreManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AnalisisService @Inject constructor(private val analisisClient: AnalisisClient,
                                          private val geminiClient: GeminiClient,
                                          private val dataStoreManager: DataStoreManager) {

    private val GEMINI_KEY = BuildConfig.GEMINI_KEY;
    private val token = dataStoreManager.getToken();

    suspend fun analizar(): PropiedadesResponse? {
        try{

            return withContext(Dispatchers.IO){
                val parsedToken = "Bearer ${token}"
                val response = analisisClient.analizar(token);
                Log.i("OSCAR", "${response}");
                response.body();
            }
        }catch (e: Exception){
            Log.e("OSCAR", "Error: ${e.message}")
            return null;
        }

    }


    suspend fun geocodificar(geocodificadorRequest: GeocodificadorRequest): GeocodificadorResponse? {

        try{
            return withContext(Dispatchers.IO){
                val response = analisisClient.geocodificar(geocodificadorRequest);
                Log.i("OSCAR", "${response}");
                response.body();
            }
        }catch (e: Exception){
            Log.e("OSCAR", "Error: ${e.message}")
            return null;
        }

    }

    suspend fun analizarGemini(geminiRequest: GeminiRequest): GeminiResponse? {
        return withContext(Dispatchers.IO){
            try {
                Log.i("OSCAR", "Llamando a Gemini 2.5 Flash...")
                val startTime = System.currentTimeMillis()

                val response = geminiClient.generateContent(GEMINI_KEY, geminiRequest)

                val duration = System.currentTimeMillis() - startTime
                Log.i("OSCAR", "Respuesta recibida en ${duration}ms")
                Log.i("OSCAR", "Tokens usados: ${response.usageMetadata?.totalTokenCount}")

                Log.i("OSCAR", "Candidates count: ${response.candidates?.size ?: 0}")
                response.candidates?.firstOrNull()?.let { candidate ->
                    Log.i("OSCAR", "First candidate finish reason: ${candidate.finishReason}")
                    Log.i("OSCAR", "Parts count: ${candidate.content?.parts?.size ?: 0}")
                    candidate.content?.parts?.firstOrNull()?.let { part ->
                        Log.i("OSCAR", "Text preview: ${part.text.take(200)}")
                    }
                }

                response
            } catch (e: retrofit2.HttpException) {
                Log.e("OSCAR", "HTTP Error ${e.code()}: ${e.message()}")
                val errorBody = e.response()?.errorBody()?.string()
                Log.e("OSCAR", "Response: $errorBody")
                null
            } catch (e: Exception) {
                Log.e("OSCAR", "Error: ${e.message}")
                e.printStackTrace()
                null
            }
        }
    }
}