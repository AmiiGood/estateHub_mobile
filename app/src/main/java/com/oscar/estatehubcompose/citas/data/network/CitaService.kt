package com.oscar.estatehubcompose.citas.data.network

import android.util.Log
import com.oscar.estatehubcompose.citas.data.network.request.CitaRequest
import com.oscar.estatehubcompose.citas.data.network.response.CitaResponse
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
                        400 -> "El dueño de la propiedad no puede agendar una cita para su propia propiedad"
                        401 -> "No autorizado. Verifica tu sesión"
                        404 -> "Propiedad no encontrada"
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
}