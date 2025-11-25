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
                val response = geminiClient.generateContent(GEMINI_KEY,geminiRequest);
                Log.i("OSCAR", response.toString());
                response;
            }catch (e: Exception){
                Log.e("OSCAR", "Error: ${e.message}")
                null
            }

        }
    };
}