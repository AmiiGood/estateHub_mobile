package com.oscar.estatehubcompose.perfil.data.network

import com.oscar.estatehubcompose.perfil.data.network.response.PropiedadesResponse
import com.oscar.estatehubcompose.perfil.data.network.response.UsuarioResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface PerfilClient {

    @GET("usuarios/getUsuario/{id}")
    suspend fun getUsuario(
        @Header("Authorization") authorization: String,
        @Path("id") id: Int?
    ): Response<UsuarioResponse?>;


    @GET("propiedades/getPropiedadesByUsuario/{id}")
    suspend fun getPropiedadesByUsuario(
        @Header("Authorization") authorization: String,
        @Path("id") id: Int?
    ): Response<PropiedadesResponse?>
}