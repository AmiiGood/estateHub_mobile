package com.oscar.estatehubcompose.perfil.data

import com.oscar.estatehubcompose.perfil.data.network.PerfilService
import com.oscar.estatehubcompose.perfil.data.network.response.PropiedadesResponse
import com.oscar.estatehubcompose.perfil.data.network.response.UsuarioResponse
import javax.inject.Inject

class PerfilRepository @Inject constructor(private var perfilService: PerfilService) {

    suspend fun getUsuario(): UsuarioResponse?{

        return perfilService.getUsuario();
    }

    suspend fun getPropiedadesByUsuario(): PropiedadesResponse?{

        return perfilService.getPropiedades();
    }

}