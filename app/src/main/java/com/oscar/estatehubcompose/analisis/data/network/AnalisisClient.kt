package com.oscar.estatehubcompose.analisis.data.network

import com.oscar.estatehubcompose.analisis.data.network.request.GeocodificadorRequest
import com.oscar.estatehubcompose.analisis.data.network.response.GeocodificadorResponse
import com.oscar.estatehubcompose.analisis.data.network.response.PropiedadesResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AnalisisClient {

    @GET("propiedades/getPropiedades")
    suspend fun analizar(
        @Header("Authorization") authorization: Flow<String?>
    ): Response<PropiedadesResponse>;

    @POST("geocodificador/getInfo")
    suspend fun geocodificar(@Body geocodificadorRequest: GeocodificadorRequest): Response<GeocodificadorResponse>

}