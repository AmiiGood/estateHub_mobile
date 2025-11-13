package com.oscar.estatehubcompose.properties.data.network

import com.oscar.estatehubcompose.properties.data.network.response.PropertyResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PropertyClient {
    @GET("propiedades/getPropiedades")
    suspend fun getPropiedades(
        @Query("ciudad") ciudad: String? = null,
        @Query("estado") estado: String? = null,
        @Query("tipoPropiedad") tipoPropiedad: String? = null,
        @Query("precioMin") precioMin: Double? = null,
        @Query("precioMax") precioMax: Double? = null,
        @Query("habitaciones") habitaciones: Int? = null,
        @Query("banios") banios: Int? = null,
        @Query("publicadoEcommerce") publicadoEcommerce: Boolean = true
    ): Response<PropertyResponse>
}