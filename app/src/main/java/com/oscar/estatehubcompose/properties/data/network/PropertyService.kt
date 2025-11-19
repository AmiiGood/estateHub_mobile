package com.oscar.estatehubcompose.properties.data.network

import android.util.Log
import com.oscar.estatehubcompose.properties.data.network.response.PropertyResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PropertyService @Inject constructor(private val propertyClient: PropertyClient) {

    suspend fun getPropiedades(
        ciudad: String? = null,
        estado: String? = null,
        tipoPropiedad: String? = null,
        precioMin: Double? = null,
        precioMax: Double? = null,
        habitaciones: Int? = null,
        banios: Int? = null
    ): PropertyResponse? {
        return withContext(Dispatchers.IO) {
            try {
                val response = propertyClient.getPropiedades(
                    ciudad = ciudad,
                    estado = estado,
                    tipoPropiedad = tipoPropiedad,
                    precioMin = precioMin,
                    precioMax = precioMax,
                    habitaciones = habitaciones,
                    banios = banios,
                    publicadoEcommerce = true
                )
                Log.i("HomeService", "Response: ${response.body()}")
                if (response.isSuccessful) {
                    response.body()
                } else {
                    Log.e("HomeService", "Error: ${response.code()} - ${response.message()}")
                    null
                }
            } catch (e: Exception) {
                Log.e("HomeService", "Exception: ${e.message}")
                null
            }
        }
    }
}