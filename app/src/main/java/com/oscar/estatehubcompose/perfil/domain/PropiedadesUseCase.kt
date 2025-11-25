package com.oscar.estatehubcompose.perfil.domain

import com.oscar.estatehubcompose.perfil.data.PerfilRepository
import com.oscar.estatehubcompose.perfil.data.network.response.PropiedadesResponse
import com.oscar.estatehubcompose.perfil.data.network.response.UsuarioResponse
import javax.inject.Inject

class PropiedadesUseCase @Inject constructor(private var perfilRepository: PerfilRepository) {

    suspend operator fun invoke(): PropiedadesResponse?{
        return perfilRepository.getPropiedadesByUsuario()
    }

}