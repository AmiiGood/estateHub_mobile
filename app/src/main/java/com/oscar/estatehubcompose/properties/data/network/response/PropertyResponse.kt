package com.oscar.estatehubcompose.properties.data.network.response

import com.google.gson.annotations.SerializedName

data class PropertyResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: List<Propiedad>,
    @SerializedName("count") val count: Int
)

data class Propiedad(
    @SerializedName("idPropiedad") val idPropiedad: Int,
    @SerializedName("idUsuario") val idUsuario: Int,
    @SerializedName("titulo") val titulo: String,
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("direccion") val direccion: String,
    @SerializedName("latitud") val latitud: String,
    @SerializedName("longitud") val longitud: String,
    @SerializedName("colonia") val colonia: String,
    @SerializedName("ciudad") val ciudad: String,
    @SerializedName("estado") val estado: String,
    @SerializedName("codigoPostal") val codigoPostal: String,
    @SerializedName("tipoPropiedad") val tipoPropiedad: String,
    @SerializedName("estatus") val estatus: String,
    @SerializedName("precioVenta") val precioVenta: Double?,
    @SerializedName("precioRenta") val precioRenta: Double?,
    @SerializedName("numHabitaciones") val numHabitaciones: Int,
    @SerializedName("numBanios") val numBanios: Int,
    @SerializedName("metrosCuadrados") val metrosCuadrados: Double,
    @SerializedName("numEstacionamiento") val numEstacionamiento: Int,
    @SerializedName("plantas") val plantas: Int,
    @SerializedName("residencial") val residencial: Boolean,
    @SerializedName("jardin") val jardin: Boolean,
    @SerializedName("alberca") val alberca: Boolean,
    @SerializedName("sotano") val sotano: Boolean,
    @SerializedName("terraza") val terraza: Boolean,
    @SerializedName("cuartoServicio") val cuartoServicio: Boolean,
    @SerializedName("muebles") val muebles: Boolean,
    @SerializedName("credito") val credito: Boolean,
    @SerializedName("fechaRegistro") val fechaRegistro: String,
    @SerializedName("publicadoEcommerce") val publicadoEcommerce: Boolean,
    @SerializedName("imagenes") val imagenes: List<Imagen>
)

data class Imagen(
    @SerializedName("idImagen") val idImagen: Int,
    @SerializedName("idPropiedad") val idPropiedad: Int,
    @SerializedName("urlImagen") val urlImagen: String,
    @SerializedName("fechaSubida") val fechaSubida: String
)