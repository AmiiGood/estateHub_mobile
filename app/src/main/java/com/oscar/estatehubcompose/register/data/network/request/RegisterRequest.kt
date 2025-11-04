package com.oscar.estatehubcompose.register.data.network.request

data class Usuario(
    val email: String,
    val password: String,
    val nombre: String,
    val apellidoPaterno: String,
    val apellidoMaterno: String,
    val telefono: String,
    val fechaRegistro: String
)

data class RegisterRequest(
    val usuario: Usuario
)