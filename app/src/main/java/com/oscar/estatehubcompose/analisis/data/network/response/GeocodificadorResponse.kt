package com.oscar.estatehubcompose.analisis.data.network.response

import com.google.gson.annotations.SerializedName

data class GeocodificadorResponse(@SerializedName("ok") var ok: Boolean,
                                 @SerializedName("precision") var precision: Int,
                                 @SerializedName("params") var params: Params) {
}


data class Params(@SerializedName("region") var region: Region,
                  @SerializedName("estado") var estado: Estado,
                  @SerializedName("municipio") var municipio: Municipio,
                  @SerializedName("ciudad") var ciudad: Ciudad,
                  @SerializedName("localidad") var localidad: Localidad,
                  @SerializedName("colonia") var colonia: Colonia,
                  @SerializedName("codigo_postal") var codigo_postal: CodigoPostal,
                  @SerializedName("calle") var calle: Calle,
                  @SerializedName("ageb") var ageb: Ageb,
                  @SerializedName("agebCompleto") var agebCompleto: AgebCompleto,
                  @SerializedName("nse") var nse: NSE,
                  @SerializedName("poi") var poi: List<POI>,
                  @SerializedName("denue") var denue: Denue){}


data class Region(@SerializedName("region") var region: String,
                  @SerializedName("id_region") var id_region: Int){}

data class Estado (@SerializedName("estado") var estado: String,
                   @SerializedName("id_estado") var id_estado: Int,
                   @SerializedName("id_region") var id_region: Int){}

data class Municipio(@SerializedName("municipio") var municipio: String,
                     @SerializedName("id_municipio") var id_municipio: Int,
                     @SerializedName("id_estado") var id_estado: Int,
                     @SerializedName("id_region") var id_region: Int,
                     @SerializedName("region") var region: String){}

data class Ciudad(@SerializedName("ciudad") var ciudad: String){}
data class Localidad(
    @SerializedName("id_localidad") var id_localidad: Int,
    @SerializedName("id_municipio") var id_municipio: Int,
    @SerializedName("id_estado") var id_estado: Int,
    @SerializedName("id_region") var id_region: Int,
    @SerializedName("localidad") var localidad: String
){};

data class Colonia(
    @SerializedName("id_colonia") var id_colonia: Int,
    @SerializedName("id_codigo_postal") var id_codigo_postal: Int,
    @SerializedName("id_municipio") var id_municipio: Int,
    @SerializedName("id_estado") var id_estado: Int,
    @SerializedName("estado") var estado: String,
    @SerializedName("municipio") var municipio: String,
    @SerializedName("colonia") var colonia: String,
    @SerializedName("codigo_postal") var codigo_postal: String
){};

data class CodigoPostal(
    @SerializedName("id_codigo_postal") var id_codigo_postal: Int,
    @SerializedName("id_municipio") var id_municipio: Int,
    @SerializedName("id_estado") var id_estado: Int,
    @SerializedName("id_region") var id_region: Int,
    @SerializedName("estado") var estado: String,
    @SerializedName("municipio") var municipio: String,
    @SerializedName("codigo_postal") var codigo_postal: String
){};

data class Calle(
    @SerializedName("id_calle") var id_calle: Int,
    @SerializedName("calle") var calle: String,
    @SerializedName("l_refaddr") var l_refaddr: String,
    @SerializedName("l_nrefaddr") var l_nrefaddr: String,
    @SerializedName("r_refaddr") var r_refaddr: String,
    @SerializedName("r_nrefaddr") var r_nrefaddr: String,
    @SerializedName("municipio") var municipio: String,
    @SerializedName("id_municipio") var id_municipio: Int,
    @SerializedName("estado") var estado: String,
    @SerializedName("id_estado") var id_estado: Int,
    @SerializedName("region") var region: String,
    @SerializedName("id_region") var id_region: Int,
    @SerializedName("func_class") var func_class: String,
    @SerializedName("distancia") var distancia: Double
){};

data class Ageb(
    @SerializedName("POLYGON_NAME") var POLYGON_NAME: String,
    @SerializedName("POBTOT") var POBTOT: Int,
    @SerializedName("POBMAS") var POBMAS: Int,
    @SerializedName("POBFEM") var POBFEM: Int,
    @SerializedName("POB15_64") var POB15_64: Int,
    @SerializedName("POB65_MAS") var POB65_MAS: Int,
    @SerializedName("PEA") var PEA: Int,
    @SerializedName("POCUPADA") var POCUPADA: Int,
    @SerializedName("PDESOCUP") var PDESOCUP: Int,
    @SerializedName("PDER_SS") var PDER_SS: Int,
    @SerializedName("PSINDER") var PSINDER: Int,
    @SerializedName("P15YM_AN") var P15YM_AN: Int,
    @SerializedName("P15SEC_COM") var P15SEC_COM: Int,
    @SerializedName("VPH_C_SERV") var VPH_C_SERV: Int,
    @SerializedName("VPH_AUTOM") var VPH_AUTOM: Int,
    @SerializedName("VPH_INTER") var VPH_INTER: Int,
    @SerializedName("PRO_OCUP_C") var PRO_OCUP_C: Int,
    @SerializedName("TVIVHAB") var TVIVHAB: Int,
    @SerializedName("VIVPAR_DES") var VIVPAR_DES: Int,
    @SerializedName("PHOGJEF_F") var PHOGJEF_F: Int,
    @SerializedName("TOTHOG") var TOTHOG: Int,
    @SerializedName("P_18A24") var P_18A24: Int,
    @SerializedName("P_60YMAS") var P_60YMAS: Int,
    @SerializedName("P6A11_NOA") var P6A11_NOA: Int,
    @SerializedName("P12A14NOA") var P12A14NOA: Int,
    @SerializedName("PDER_IMSS") var PDER_IMSS: Int,
    @SerializedName("PDER_ISTE") var PDER_ISTE: Int,
    @SerializedName("VPH_CEL") var VPH_CEL: Int,
    @SerializedName("VPH_LAVAD") var VPH_LAVAD: Int,
    @SerializedName("VPH_REFRI") var VPH_REFRI: Int
){};

data class AgebCompleto(@SerializedName("ageb") var ageb: String){}

data class NSE(
    @SerializedName("id_nse") var id_nse: Int,
    @SerializedName("id_colonia") var id_colonia: Int,
    @SerializedName("id_municipio") var id_municipio: Int,
    @SerializedName("id_estado") var id_estado: Int,
    @SerializedName("id_region") var id_region: Int,
    @SerializedName("colonia") var colonia: String,
    @SerializedName("codigo_postal") var codigo_postal: String,
    @SerializedName("nse") var nse: String,
    @SerializedName("estado") var estado: String,
    @SerializedName("municipio") var municipio: String
){};

data class POI(
    @SerializedName("id_poi") var id_poi: Int,
    @SerializedName("Nom_estab") var Nom_estab: String,
    @SerializedName("numero") var numero: String,
    @SerializedName("codigo_postal") var codigo_postal: String,
    @SerializedName("calle") var calle: String,
    @SerializedName("municipio") var municipio: String,
    @SerializedName("estado") var estado: String,
    @SerializedName("region") var region: String,
    @SerializedName("clase") var clase: String
){};

data class Denue(@SerializedName("denue") var denue: String){};

