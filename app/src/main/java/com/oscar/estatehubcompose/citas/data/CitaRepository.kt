package com.oscar.estatehubcompose.citas.data

import com.oscar.estatehubcompose.citas.data.network.CitaService
import com.oscar.estatehubcompose.citas.data.network.request.CitaRequest
import com.oscar.estatehubcompose.citas.data.network.response.CitaResponse
import javax.inject.Inject

class CitaRepository @Inject constructor(
    private val citaService: CitaService
) {
    suspend fun createCita(token: String, citaRequest: CitaRequest): Result<CitaResponse> {
        return citaService.createCita(token, citaRequest)
    }
}