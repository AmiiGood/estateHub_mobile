package com.oscar.estatehubcompose.analisis.domain

import com.oscar.estatehubcompose.analisis.Models.GeocodificadorInfo
import com.oscar.estatehubcompose.analisis.data.AnalisisRepository
import com.oscar.estatehubcompose.analisis.data.network.request.GeocodificadorRequest
import com.oscar.estatehubcompose.analisis.data.network.response.GeocodificadorResponse
import javax.inject.Inject

class geocodificadorUseCase @Inject constructor(private val analisisRepository: AnalisisRepository){

    suspend operator fun invoke(geocodificadorRequest: GeocodificadorRequest): GeocodificadorInfo?{
        var geocodificadorResponse = analisisRepository.geocodificar(geocodificadorRequest);
        return geocodificadorResponse
    }
}