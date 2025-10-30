package com.oscar.estatehubcompose.login.data.network.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(@SerializedName("message") val message: String,
                         @SerializedName("sucess") val success: Boolean,
                         @SerializedName("token") val token: String) {
}