package com.oscar.estatehubcompose.register.data.network

import com.oscar.estatehubcompose.register.data.network.request.RegisterRequest
import com.oscar.estatehubcompose.register.data.network.response.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RegisterClient {
    @POST("usuarios/postUsuario")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<RegisterResponse>
}