package com.oscar.estatehubcompose.perfil.data.network.response

import com.google.gson.annotations.SerializedName

data class UsuarioResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: Usuario
)

data class Usuario(
    @SerializedName("idUsuario")
    val idUsuario: Int,

    @SerializedName("email")
    val email: String,

    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("apellidoPaterno")
    val apellidoPaterno: String,

    @SerializedName("apellidoMaterno")
    val apellidoMaterno: String,

    @SerializedName("telefono")
    val telefono: String,

    @SerializedName("fechaRegistro")
    val fechaRegistro: String,

    @SerializedName("activo")
    val activo: Boolean
)
