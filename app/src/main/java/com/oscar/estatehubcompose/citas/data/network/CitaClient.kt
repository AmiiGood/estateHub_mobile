package com.oscar.estatehubcompose.citas.data.network

import com.oscar.estatehubcompose.citas.data.network.request.CitaRequest
import com.oscar.estatehubcompose.citas.data.network.response.CitaResponse
import com.oscar.estatehubcompose.citas.data.network.response.HorariosDisponiblesResponse
import retrofit2.Response
import retrofit2.http.*

interface CitaClient {
    @POST("citas/postCita")
    suspend fun createCita(
        @Header("Authorization") token: String,
        @Body citaRequest: CitaRequest
    ): Response<CitaResponse>

    @GET("citas/getHorariosDisponibles")
    suspend fun getHorariosDisponibles(
        @Query("idPropiedad") idPropiedad: Int,
        @Query("fecha") fecha: String
    ): Response<HorariosDisponiblesResponse>
}