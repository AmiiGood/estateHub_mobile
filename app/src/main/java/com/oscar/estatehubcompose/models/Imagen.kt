package com.oscar.estatehubcompose.models

import com.google.gson.annotations.SerializedName
import java.util.Date

data class Imagen(
    @SerializedName("idImagen")
    val idImagen: Int,

    @SerializedName("idPropiedad")
    val idPropiedad: Int,

    @SerializedName("urlImagen")
    val urlImagen: String,

    @SerializedName("fechaSubida")
    val fechaSubida: String,

    @SerializedName("deletedAt")
    val deletedAt: String?
)