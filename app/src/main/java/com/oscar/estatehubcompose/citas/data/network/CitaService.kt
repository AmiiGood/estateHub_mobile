package com.oscar.estatehubcompose.citas.data.network

import android.util.Log
import com.oscar.estatehubcompose.citas.data.network.request.CitaRequest
import com.oscar.estatehubcompose.citas.data.network.response.CitaResponse
import com.oscar.estatehubcompose.citas.data.network.response.HorariosDisponiblesResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CitaService @Inject constructor(
    private val citaClient: CitaClient
) {
    suspend fun createCita(token: String, citaRequest: CitaRequest): Result<CitaResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = citaClient.createCita("Bearer $token", citaRequest)
                Log.i("CitaService", "Response: ${response.body()}")

                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    val errorMessage = when (response.code()) {
                        400 -> "Datos inválidos. Verifica la fecha y hora."
                        403 -> "No tienes permiso para realizar esta acción."
                        404 -> "Propiedad o usuario no encontrado."
                        409 -> "Este horario ya está ocupado. Por favor elige otro."
                        else -> "Error ${response.code()}: ${response.message()}"
                    }
                    Result.failure(Exception(errorMessage))
                }
            } catch (e: Exception) {
                Log.e("CitaService", "Exception: ${e.message}")
                Result.failure(e)
            }
        }
    }

    suspend fun getHorariosDisponibles(
        idPropiedad: Int,
        fecha: String
    ): HorariosDisponiblesResponse? {
        return withContext(Dispatchers.IO) {
            try {
                val response = citaClient.getHorariosDisponibles(idPropiedad, fecha)
                Log.i("CitaService", "Horarios Response: ${response.body()}")

                if (response.isSuccessful) {
                    response.body()
                } else {
                    Log.e("CitaService", "Error: ${response.code()} - ${response.message()}")
                    null
                }
            } catch (e: Exception) {
                Log.e("CitaService", "Exception: ${e.message}")
                null
            }
        }
    }
}