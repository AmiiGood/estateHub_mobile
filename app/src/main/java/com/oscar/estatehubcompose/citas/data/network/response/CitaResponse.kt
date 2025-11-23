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

data class HorariosDisponiblesResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("fecha") val fecha: String,
    @SerializedName("data") val data: List<HorarioDisponible>,
    @SerializedName("count") val count: Int
)

data class HorarioDisponible(
    @SerializedName("fecha") val fecha: String,
    @SerializedName("horaLocal") val horaLocal: String,
    @SerializedName("disponible") val disponible: Boolean
)