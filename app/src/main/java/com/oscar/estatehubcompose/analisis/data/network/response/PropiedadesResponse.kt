package com.oscar.estatehubcompose.analisis.data.network.response

data class PropiedadesResponse(
    val success: Boolean,
    val data: List<Propiedad>,
    val count: Int
)

data class Propiedad(
    val idPropiedad: Int,
    val idUsuario: Int,
    val titulo: String,
    val descripcion: String,
    val direccion: String,
    val latitud: String,
    val longitud: String,
    val colonia: String,
    val ciudad: String,
    val estado: String,
    val codigoPostal: String,
    val tipoPropiedad: String,
    val estatus: String,
    val precioVenta: Int,
    val precioRenta: Int,
    val numHabitaciones: Int,
    val numBanios: Int,
    val metrosCuadrados: Int,
    val numEstacionamiento: Int,
    val plantas: Int,
    val residencial: Boolean,
    val jardin: Boolean,
    val alberca: Boolean,
    val sotano: Boolean,
    val terraza: Boolean,
    val cuartoServicio: Boolean,
    val muebles: Boolean,
    val credito: Boolean,
    val fechaRegistro: String,
    val publicadoEcommerce: Boolean,
    val deletedAt: String?,
    val imagenes: List<Imagen>
)

data class Imagen(
    val idImagen: Int,
    val idPropiedad: Int,
    val urlImagen: String,
    val fechaSubida: String,
    val deletedAt: String?
)
