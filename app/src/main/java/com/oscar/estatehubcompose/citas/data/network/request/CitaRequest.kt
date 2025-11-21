package com.oscar.estatehubcompose.citas.data.network.request

import com.google.gson.annotations.SerializedName

data class CitaRequest(
    @SerializedName("cita")
    val cita: CitaData
)

data class CitaData(
    @SerializedName("idPropiedad")
    val idPropiedad: Int,

    @SerializedName("idUsuario")
    val idUsuario: Int,

    @SerializedName("fecha")
    val fecha: String,

    @SerializedName("estatus")
    val estatus: String = "en_proceso"
)