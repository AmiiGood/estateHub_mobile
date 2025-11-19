    package com.oscar.estatehubcompose.analisis.data

    import com.oscar.estatehubcompose.analisis.Models.GeocodificadorInfo
    import com.oscar.estatehubcompose.analisis.data.network.AnalisisService
    import com.oscar.estatehubcompose.analisis.data.network.request.AnalisisRequest
    import com.oscar.estatehubcompose.analisis.data.network.request.Content
    import com.oscar.estatehubcompose.analisis.data.network.request.GeminiRequest
    import com.oscar.estatehubcompose.analisis.data.network.request.GeocodificadorRequest
    import com.oscar.estatehubcompose.analisis.data.network.request.Part
    import com.oscar.estatehubcompose.analisis.data.network.response.AnalisisResponse
    import com.oscar.estatehubcompose.analisis.data.network.response.GeminiResponse
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


        suspend fun geminiAnalizar(
            colonia: String,
            codigoPostal: String,
            ciudad: String,
            estado: String,
            geocodificadorInfo: GeocodificadorInfo?
        ): GeminiResponse? {

            val prompt = """
    colonia: $colonia cp: $codigoPostal ciudad: $ciudad estado: $estado

    info_demografica: ${geocodificadorInfo}
    INSTRUCCIONES CRÍTICAS:
    1. Tu respuesta DEBE ser ÚNICAMENTE un objeto JSON válido, sin explicaciones.
    2. NO uses bloques de código markdown (``````).
    3. La primera línea debe empezar con { y la última con }.
    4. Basa los precios en promedios para México en 2025.

    TAREA: Genera una estimación de precios y oportunidades de negocio para la ubicación proporcionada.

    FORMATO REQUERIDO (responde SOLO esto):
    {
      "compra": {
        "casa": 3850000,
        "local_comercial": 4200000,
        "departamento": 2950000
      },
      "renta": {
        "casa": 22000,
        "local_comercial": 28000,
        "departamento": 16500
      },
      "recomendacion_negocio": [
        {
          "sector": "Comercio de proximidad",
          "oportunidad": "Mini-supermercado o tienda de conveniencia 24h",
          "descripcion": "Dada la densidad habitacional inferida y el perfil residencial, existe alta demanda recurrente de insumos básicos sin necesidad de grandes traslados."
        },
        {
          "sector": "Servicios de salud",
          "oportunidad": "Farmacia con consultorio médico adyacente",
          "descripcion": "El perfil demográfico sugiere una necesidad constante de servicios de salud de primer contacto, ideal para locales en esquinas o avenidas principales."
        }
      ],
      "rentabilidad_neta": {
        "local_comercial": "7.5% - 8.5%",
        "departamento": "5.5% - 6.2%",
        "casa": "4.8% - 5.5%"
      },
      "plusvalia_recomendada": {
        "tendencia": "Alza moderada sostenida",
        "aumento_valor_aprox_5_anios": "18% - 22%"
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