package com.oscar.estatehubcompose.analisis.data

import com.oscar.estatehubcompose.analisis.data.network.AnalisisService
import com.oscar.estatehubcompose.analisis.data.network.request.AnalisisRequest
import com.oscar.estatehubcompose.analisis.data.network.response.AnalisisResponse
import javax.inject.Inject

class AnalisisRepository @Inject constructor(private val analisisService: AnalisisService){

    suspend fun analizar(analisisRequest: AnalisisRequest): AnalisisResponse? {

        val response = analisisService.analizar(analisisRequest);

        return response;

    }

}