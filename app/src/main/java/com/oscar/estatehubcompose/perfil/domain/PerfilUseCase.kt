package com.oscar.estatehubcompose.perfil.domain

import com.oscar.estatehubcompose.perfil.data.PerfilRepository
import com.oscar.estatehubcompose.perfil.data.network.response.UsuarioResponse
import javax.inject.Inject

class PerfilUseCase @Inject constructor(private var perfilRepository: PerfilRepository) {

    suspend operator fun invoke(): UsuarioResponse?{
        return perfilRepository.getUsuario();
    }

}