package com.oscar.estatehubcompose.properties.domain

import com.oscar.estatehubcompose.properties.data.PropertyRepository
import com.oscar.estatehubcompose.properties.data.network.response.PropertyResponse
import javax.inject.Inject

class PropertyUseCase @Inject constructor(private val propertyRepository: PropertyRepository) {

    suspend operator fun invoke(
        ciudad: String? = null,
        estado: String? = null,
        tipoPropiedad: String? = null,
        precioMin: Double? = null,
        precioMax: Double? = null,
        habitaciones: Int? = null,
        banios: Int? = null
    ): PropertyResponse? {
        return propertyRepository.getPropiedades(
            ciudad = ciudad,
            estado = estado,
            tipoPropiedad = tipoPropiedad,
            precioMin = precioMin,
            precioMax = precioMax,
            habitaciones = habitaciones,
            banios = banios
        )
    }
}