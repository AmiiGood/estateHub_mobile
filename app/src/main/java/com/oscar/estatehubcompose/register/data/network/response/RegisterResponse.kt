package com.oscar.estatehubcompose.register.data.network.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("success")
    val success: Boolean? = null,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("data")
    val data: UsuarioData? = null
)

data class UsuarioData(
    @SerializedName("id")
    val id: Int?,

    @SerializedName("email")
    val email: String?,

    @SerializedName("nombre")
    val nombre: String?
)