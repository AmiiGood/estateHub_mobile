package com.oscar.estatehubcompose.analisis.data.network

import com.oscar.estatehubcompose.analisis.data.network.request.AnalisisRequest
import com.oscar.estatehubcompose.analisis.data.network.response.AnalisisResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AnalisisClient {

    @POST
    suspend fun analizar(@Body punto: AnalisisRequest): Response<AnalisisResponse>;
}