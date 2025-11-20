package com.oscar.estatehubcompose.analisis.domain

import com.oscar.estatehubcompose.analisis.data.AnalisisRepository
import com.oscar.estatehubcompose.analisis.data.network.request.AnalisisRequest
import com.oscar.estatehubcompose.analisis.data.network.request.GeocodificadorRequest
import com.oscar.estatehubcompose.analisis.data.network.response.GeocodificadorResponse
import com.oscar.estatehubcompose.analisis.data.network.response.PropiedadesResponse
import javax.inject.Inject

class AnalisisUseCase @Inject constructor(private val analisisRepository: AnalisisRepository) {

    suspend operator fun invoke(): PropiedadesResponse?{
        var analisisResponse = analisisRepository.analizar();
        return analisisResponse;
    }



}