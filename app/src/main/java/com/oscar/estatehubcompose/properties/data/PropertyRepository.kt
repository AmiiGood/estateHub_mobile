package com.oscar.estatehubcompose.properties.data

import com.oscar.estatehubcompose.properties.data.network.PropertyService
import com.oscar.estatehubcompose.properties.data.network.response.PropertyResponse
import javax.inject.Inject

class PropertyRepository @Inject constructor(private val propertyService: PropertyService) {

    suspend fun getPropiedades(
        ciudad: String? = null,
        estado: String? = null,
        tipoPropiedad: String? = null,
        precioMin: Double? = null,
        precioMax: Double? = null,
        habitaciones: Int? = null,
        banios: Int? = null
    ): PropertyResponse? {
        return propertyService.getPropiedades(
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