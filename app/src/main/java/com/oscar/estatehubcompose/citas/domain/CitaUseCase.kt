package com.oscar.estatehubcompose.citas.domain

import com.oscar.estatehubcompose.citas.data.CitaRepository
import com.oscar.estatehubcompose.citas.data.network.request.CitaRequest
import com.oscar.estatehubcompose.citas.data.network.response.CitaResponse
import com.oscar.estatehubcompose.citas.data.network.response.HorariosDisponiblesResponse
import javax.inject.Inject

class CitaUseCase @Inject constructor(
    private val citaRepository: CitaRepository
) {
    suspend operator fun invoke(token: String, citaRequest: CitaRequest): Result<CitaResponse> {
        return citaRepository.createCita(token, citaRequest)
    }
}

class GetHorariosDisponiblesUseCase @Inject constructor(
    private val citaRepository: CitaRepository
) {
    suspend operator fun invoke(
        idPropiedad: Int,
        fecha: String
    ): HorariosDisponiblesResponse? {
        return citaRepository.getHorariosDisponibles(idPropiedad, fecha)
    }
}