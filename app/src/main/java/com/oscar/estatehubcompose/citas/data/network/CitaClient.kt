package com.oscar.estatehubcompose.citas.data.network

import com.oscar.estatehubcompose.citas.data.network.request.CitaRequest
import com.oscar.estatehubcompose.citas.data.network.response.CitaResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface CitaClient {
    @POST("citas/postCita")
    suspend fun createCita(
        @Header("Authorization") token: String,
        @Body citaRequest: CitaRequest
    ): Response<CitaResponse>
}