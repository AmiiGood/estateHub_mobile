package com.oscar.estatehubcompose.analisis.data.network

import android.util.Log
import com.oscar.estatehubcompose.analisis.data.network.request.AnalisisRequest
import com.oscar.estatehubcompose.analisis.data.network.request.GeocodificadorRequest
import com.oscar.estatehubcompose.analisis.data.network.response.AnalisisResponse
import com.oscar.estatehubcompose.analisis.data.network.response.GeocodificadorResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Response
import javax.inject.Inject

class AnalisisService @Inject constructor(private val analisisClient: AnalisisClient,) {


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
}