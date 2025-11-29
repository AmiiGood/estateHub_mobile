    package com.oscar.estatehubcompose.analisis.data

    import com.oscar.estatehubcompose.analisis.Models.GeocodificadorInfo
    import com.oscar.estatehubcompose.analisis.data.network.AnalisisService
    import com.oscar.estatehubcompose.analisis.data.network.request.AnalisisRequest
    import com.oscar.estatehubcompose.analisis.data.network.request.Content
    import com.oscar.estatehubcompose.analisis.data.network.request.GeminiRequest
    import com.oscar.estatehubcompose.analisis.data.network.request.GenerationConfig
    import com.oscar.estatehubcompose.analisis.data.network.request.GeocodificadorRequest
    import com.oscar.estatehubcompose.analisis.data.network.request.Part
    import com.oscar.estatehubcompose.analisis.data.network.response.GeminiResponse
    import com.oscar.estatehubcompose.analisis.data.network.response.PropiedadesResponse
    import javax.inject.Inject

    class AnalisisRepository @Inject constructor(private val analisisService: AnalisisService){

        suspend fun analizar(): PropiedadesResponse? {
            return analisisService.analizar();
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
ANALIZA ESTA UBICACIÓN ESPECÍFICA:
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
Colonia: $colonia
CP: $codigoPostal
Ciudad: $ciudad
Estado: $estado

DATOS DEMOGRÁFICOS (USA ESTOS NÚMEROS):
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
Población total: ${geocodificadorInfo?.ponlacionTotal ?: 0}
NSE: ${geocodificadorInfo?.nse ?: "No especificado"}
Empleados: ${geocodificadorInfo?.empleados ?: 0}
Viviendas habitadas: ${geocodificadorInfo?.viviendas_habitadas ?: 0}
Hombres: ${geocodificadorInfo?.hombres ?: 0}
Mujeres: ${geocodificadorInfo?.mujeres ?: 0}
Población 15-64: ${geocodificadorInfo?.quince_seisCuatro ?: 0}
Población +60: ${geocodificadorInfo?.mas_sesenta ?: 0}

NEGOCIOS EXISTENTES EN LA ZONA:
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
Salud: ${geocodificadorInfo?.hospitales_farmacias ?: 0}
Restaurantes: ${geocodificadorInfo?.restaurantes ?: 0}
Educación: ${geocodificadorInfo?.educacion ?: 0}
Financieros: ${geocodificadorInfo?.financiero ?: 0}
Parques: ${geocodificadorInfo?.parques ?: 0}
Entretenimiento: ${geocodificadorInfo?.entretenimiento ?: 0}
Comercios: ${geocodificadorInfo?.negocios ?: 0}

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
INSTRUCCIONES DE PRECIOS (MUY IMPORTANTE):
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

CALCULA los precios basándote en:

1. NSE (Nivel Socioeconómico):
   • A/B (Alto): casas 2,500,000-4,500,000, deptos 1,800,000-3,000,000
   • C+ (Medio-Alto): casas 1,800,000-2,800,000, deptos 1,200,000-2,000,000
   • C (Medio): casas 1,200,000-2,000,000, deptos 800,000-1,400,000
   • C- (Medio-Bajo): casas 800,000-1,500,000, deptos 600,000-1,000,000
   • D+ (Bajo): casas 500,000-1,000,000, deptos 400,000-800,000
   • D (Muy Bajo): casas 300,000-700,000, deptos 250,000-500,000

2. Población:
   • <1,000 habitantes: reduce precios 20%
   • 1,000-5,000: precios base
   • 5,000-15,000: aumenta 10%
   • >15,000: aumenta 20%

3. Rentas = 0.5% al 0.7% del valor de compra mensual

4. Locales comerciales:
   • Muchos comercios (>20): reduce 15%
   • Pocos comercios (<10): aumenta 20%

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
FORMATO DE RESPUESTA:
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

{
  "compra": {
    "casa": [NÚMERO calculado según NSE y población],
    "local_comercial": [NÚMERO calculado según zona comercial],
    "departamento": [NÚMERO calculado según NSE]
  },
  "renta": {
    "casa": [0.5-0.7% del precio de compra],
    "local_comercial": [0.6-0.8% del precio],
    "departamento": [0.5-0.7% del precio]
  },
  "recomendacion_negocio": [
    {
      "sector": "Sector basado en los GAPS de negocios existentes",
      "oportunidad": "Negocio específico que falta en la zona",
      "descripcion": "MENCIONA los números exactos: población ${geocodificadorInfo?.ponlacionTotal}, empleados ${geocodificadorInfo?.empleados}, número de [tipo de negocio] existentes, etc."
    },
    {
      "sector": "Otro sector diferente",
      "oportunidad": "Otro negocio necesario",
      "descripcion": "CITA los datos demográficos reales de esta zona específica"
    }
  ],
  "rentabilidad_neta": {
    "local_comercial": "[5-9%]",
    "departamento": "[4-7%]",
    "casa": "[3-6%]"
  },
  "plusvalia_recomendada": {
    "tendencia": "Analiza el desarrollo de ESTA zona específica",
    "aumento_valor_aprox_5_anios": "[8-25% según potencial]"
  }
}

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
REGLAS ESTRICTAS:
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
Precios = números enteros únicos (ej: 1250000)
NO uses rangos, solo UN número promedio
NO uses comas, puntos ni símbolos en números
Percentajes como strings (ej: "6.5%")
DEBES usar los datos demográficos específicos arriba
Cada recomendación DEBE mencionar números reales
NO copies valores de ejemplo
NO uses arrays en precios
NO uses markdown
""".trimIndent()

            val request = GeminiRequest(
                contents = listOf(Content(parts = listOf(Part(prompt)))),
                generationConfig = GenerationConfig(
                    temperature = 0.7,
                    maxOutputTokens = 2048,
                    topP = 0.95,
                    topK = 40
                )
            )

            return analisisService.analizarGemini(request)
        }

    }