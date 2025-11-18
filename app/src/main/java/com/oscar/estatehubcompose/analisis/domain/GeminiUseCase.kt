package com.oscar.estatehubcompose.analisis.domain

import com.oscar.estatehubcompose.analisis.data.AnalisisRepository
import com.oscar.estatehubcompose.analisis.data.network.request.GeminiRequest
import com.oscar.estatehubcompose.analisis.data.network.response.GeminiResponse
import javax.inject.Inject

class GeminiUseCase @Inject constructor(private val analisisRepository: AnalisisRepository) {

    suspend operator fun invoke(colonia: String, codigoPostal: String, ciudad:String, estado:String): GeminiResponse?{
        return analisisRepository.geminiAnalizar(colonia, codigoPostal, ciudad, estado);
    }

}