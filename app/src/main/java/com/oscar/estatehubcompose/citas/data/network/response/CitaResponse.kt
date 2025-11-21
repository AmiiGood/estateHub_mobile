package com.oscar.estatehubcompose.citas.data.network.response

import com.google.gson.annotations.SerializedName
import com.oscar.estatehubcompose.models.Cita

data class CitaResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: Cita?
)