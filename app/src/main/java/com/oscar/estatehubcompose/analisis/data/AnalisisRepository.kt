    package com.oscar.estatehubcompose.analisis.data

    import android.util.Log
    import androidx.lifecycle.LiveData
    import androidx.lifecycle.MutableLiveData
    import com.oscar.estatehubcompose.analisis.Models.GeocodificadorInfo
    import com.oscar.estatehubcompose.analisis.data.network.AnalisisService
    import com.oscar.estatehubcompose.analisis.data.network.request.AnalisisRequest
    import com.oscar.estatehubcompose.analisis.data.network.request.Content
    import com.oscar.estatehubcompose.analisis.data.network.request.GeminiRequest
    import com.oscar.estatehubcompose.analisis.data.network.request.GeocodificadorRequest
    import com.oscar.estatehubcompose.analisis.data.network.request.Part
    import com.oscar.estatehubcompose.analisis.data.network.response.AnalisisResponse
    import com.oscar.estatehubcompose.analisis.data.network.response.GeminiResponse
    import com.oscar.estatehubcompose.analisis.data.network.response.GeocodificadorResponse
    import javax.inject.Inject

    class AnalisisRepository @Inject constructor(private val analisisService: AnalisisService){

        suspend fun analizar(analisisRequest: AnalisisRequest): AnalisisResponse? {
            return analisisService.analizar(analisisRequest);
        }

        suspend fun geocodificar(geocodificadorRequest: GeocodificadorRequest): GeocodificadorInfo? {
            val response = analisisService.geocodificar(geocodificadorRequest)

            if (response == null) {
                return null
            }

            var autos = 0
            var negocios = 0
            var salud = 0
            var parques = 0
            var financieras = 0
            var entretenimiento = 0
            var educacion = 0
            var estacionamientos = 0
            var restaurantes = 0
            var otros = 0

            response.params?.poi?.forEach { poi ->
                when (poi.clase) {
                    "Autos y servicios" -> autos++
                    "Negocios" -> negocios++
                    "Hospitales y Farmacias" -> salud++
                    "Parques Recreativos" -> parques++
                    "Instituciones Financieras" -> financieras++
                    "Entretenimiento" -> entretenimiento++
                    "Instituciones Educativas" -> educacion++
                    "Estacionamientos" -> estacionamientos++
                    "Restaurantes" -> restaurantes++
                    "Otras Categorias" -> otros++
                }
            }

            var geocodificadorInfo: GeocodificadorInfo = GeocodificadorInfo(
                response.params?.region?.region ?: "No especificado",
                response.params?.estado?.estado ?: "No especificado",
                response.params?.municipio?.municipio ?: "No especificado",
                response.params?.localidad?.localidad ?: "No especificado",
                response.params?.colonia?.colonia ?: "No especificado",
                response.params?.codigo_postal?.codigo_postal ?: "No especificado",
                response.params?.calle?.calle ?: "No especificado",
                response.params?.ageb?.POBTOT ?: 0,
                response.params?.ageb?.POBMAS ?: 0,
                response.params?.ageb?.POBFEM ?: 0,
                response.params?.ageb?.POB15_64 ?: 0,
                response.params?.ageb?.POB65_MAS ?: 0,
                response.params?.ageb?.P_18A24 ?: 0,
                response.params?.ageb?.P_60YMAS ?: 0,
                response.params?.ageb?.P15YM_AN ?: 0,
                response.params?.ageb?.P15SEC_COM ?: 0,
                response.params?.ageb?.P6A11_NOA ?: 0,
                response.params?.ageb?.PEA ?: 0,
                response.params?.ageb?.TVIVHAB ?: 0,
                response.params?.ageb?.VPH_AUTOM ?: 0,
                response.params?.nse?.nse ?: "No especificado",
                autos,
                negocios,
                salud,
                parques,
                financieras,
                entretenimiento,
                educacion,
                estacionamientos,
                restaurantes,
                otros
            )

            return geocodificadorInfo
        }


        suspend fun geminiAnalizar(colonia: String,
                                   codigoPostal: String,
                                   ciudad:String,
                                   estado:String): GeminiResponse? {

            val prompt = """
        colonia: $colonia cp: $codigoPostal ciudad: $ciudad estado: $estado
        
        1. TAREA: Realiza un análisis de precios promedio (compra/renta), rentabilidad (Cap Rate), plusvalía inferida y genera recomendaciones de negocio para la zona de búsqueda.
        
        2. DATOS DE ENTRADA: La zona de búsqueda y los datos socioeconómicos serán proporcionados a continuación en el input.
        
        3. FORMATO DE SALIDA REQUERIDO: SIEMPRE devolverás la respuesta en el siguiente formato JSON, sin excepción, reemplazando los valores (0, "", etc.) por los datos analizados:
        
        {
          "compra": {
            "casa": 0,
            "local_comercial": 0,
            "departamento": 0
          },
          "renta": {
            "casa": 0,
            "local_comercial": 0,
            "departamento": 0
          },
          "recomendacion_negocio": [
            {
              "sector": "",
              "oportunidad": "",
              "descripcion": ""
            }
          ],
          "rentabilidad_neta": {
            "explicacion_de_que_es": "",
            "local_comercial": "",
            "departamento": "",
            "casa": ""
          },
          "plusvalia_recomendada": {
            "tendencia": "",
            "explicacion_sencilla": "",
            "aumento_valor_aprox_5_anios": ""
          }
        }
    """.trimIndent()
            val request = GeminiRequest(contents = listOf(
                Content(
                    parts = listOf(Part(prompt))
                )
            ));

            return analisisService.analizarGemini(request);

        }

    }