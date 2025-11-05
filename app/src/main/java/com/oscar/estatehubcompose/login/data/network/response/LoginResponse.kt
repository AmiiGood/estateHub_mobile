package com.oscar.estatehubcompose.login.data.network.response

import com.google.gson.annotations.SerializedName
import com.oscar.estatehubcompose.models.Usuario

data class LoginResponse(@SerializedName("message") val message: String,
                         @SerializedName("token") val token: String,
                         @SerializedName("usuario")val usuario: Usuario) {
}