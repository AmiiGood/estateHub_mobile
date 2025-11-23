package com.oscar.estatehubcompose.models

import com.google.gson.annotations.SerializedName

data class Cita(
    @SerializedName("idCita")
    val idCita: Int,

    @SerializedName("idPropiedad")
    val idPropiedad: Int,

    @SerializedName("idUsuario")
    val idUsuario: Int,

    @SerializedName("fecha")
    val fecha: String,

    @SerializedName("estatus")
    val estatus: String,

    @SerializedName("fechaCreacion")
    val fechaCreacion: String,

    @SerializedName("deletedAt")
    val deletedAt: String?
)