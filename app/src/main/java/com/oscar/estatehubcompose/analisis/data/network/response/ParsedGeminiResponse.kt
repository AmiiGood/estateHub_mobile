package com.oscar.estatehubcompose.analisis.data.network.response

data class ParsedGeminiResponse(var compra: Compra,
                                var renta: Renta,
                                var recomendacion_negocio: List<Recomendacion_negocio>,
                                var rentabilidad_neta: Rentabilidad_neta,
                                var plusvalia_recomendada: Plusvalia_recomendada) {
}

data class Compra (var casa: Double, var local_comercial: Double, var departamento: Double){}

data class Renta(var casa: Double, var local_comercial: Double, var departamento: Double){}

data class Recomendacion_negocio (var sector: String, var oportunidad: String, var descripcion: String){}

data class Rentabilidad_neta (var tendencia: String, var explicacion_sencilla: String, var aumento_valor_aprox_5_anios: String){}

data class Plusvalia_recomendada(var tendencia: String, var explicacion_sencilla: String, var aumento_valor_aprox_5_anios: String){}