package com.oscar.estatehubcompose.analisis.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oscar.estatehubcompose.analisis.Models.GeocodificadorInfo
import com.oscar.estatehubcompose.analisis.data.network.request.GeocodificadorRequest
import com.oscar.estatehubcompose.analisis.data.network.response.ParsedGeminiResponse
import com.oscar.estatehubcompose.analisis.data.network.response.PropiedadesResponse
import com.oscar.estatehubcompose.analisis.domain.AnalisisUseCase
import com.oscar.estatehubcompose.analisis.domain.GeminiUseCase
import com.oscar.estatehubcompose.analisis.domain.geocodificadorUseCase
import com.oscar.estatehubcompose.helpers.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class AnalisisViewModel @Inject constructor(
    private val analisisUseCase: AnalisisUseCase,
    private val geocodificadorUseCase: geocodificadorUseCase,
    private val geminiUseCase: GeminiUseCase,
    private val dataStoreManager: DataStoreManager
): ViewModel(){

    private var _data = MutableLiveData<GeocodificadorInfo?>()
    var data: LiveData<GeocodificadorInfo?> = _data

    private var _dataGemini = MutableLiveData<ParsedGeminiResponse?>()
    var dataGemini: LiveData<ParsedGeminiResponse?> = _dataGemini

    private var _isGemini = MutableLiveData<Boolean>()
    var isGemini: LiveData<Boolean> = _isGemini

    private var _isLoadingGemini = MutableLiveData<Boolean>()
    var isLoadingGemini: LiveData<Boolean> = _isLoadingGemini

    private var _propiedades = MutableLiveData<PropiedadesResponse?>()
    var propiedades: LiveData<PropiedadesResponse?> = _propiedades

    private var _latitud = MutableLiveData<Double>()
    private var _longitud = MutableLiveData<Double>()
    private var _codigoPostal = MutableLiveData<Int>()

    private val geminiCache = mutableMapOf<String, ParsedGeminiResponse>()
    private val cacheTimestamps = mutableMapOf<String, Long>()
    private val CACHE_DURATION = 3600000L

    fun setLatitud(latitud: Double){
        _latitud.value = latitud
    }

    fun setLongitud(longitud: Double){
        _longitud.value = longitud
    }

    fun setCodigoPostal(codigoPostal: Int){
        _codigoPostal.value = codigoPostal
    }

    fun resetDataGemini(){
        _dataGemini.value = null
        _isLoadingGemini.value = false
    }

    fun getData(latitud: Double, longitud: Double){
        var geocodificadorRequest = GeocodificadorRequest(latitud, longitud, listOf(1,2,3))

        viewModelScope.launch {
            val response = geocodificadorUseCase.invoke(geocodificadorRequest)
            _data.value = response
            Log.i("OSCAR", data.value.toString())
        }
    }

    fun analizarGemini(
        colonia: String,
        codigoPostal: String,
        ciudad: String,
        estado: String,
        geocodificadorInfo: GeocodificadorInfo?
    ){
        viewModelScope.launch {
            val cacheKey = "$codigoPostal-$colonia"
            val now = System.currentTimeMillis()

            _isLoadingGemini.value = true
            _dataGemini.value = null

            cacheTimestamps[cacheKey]?.let { timestamp ->
                if (now - timestamp < CACHE_DURATION) {
                    geminiCache[cacheKey]?.let { cached ->
                        Log.i("OSCAR", "Usando caché (${(now - timestamp) / 1000}s antiguo)")
                        _dataGemini.value = cached
                        _isGemini.value = true
                        _isLoadingGemini.value = false
                        return@launch
                    }
                }
            }

            Log.i("OSCAR", "Realizando nueva consulta a Gemini...")
            val response = geminiUseCase.invoke(colonia, codigoPostal, ciudad, estado, geocodificadorInfo)

            _isLoadingGemini.value = false

            if (response == null){
                _isGemini.value = false
                _dataGemini.value = null
            } else {
                _isGemini.value = true
                _dataGemini.value = response
                geminiCache[cacheKey] = response
                cacheTimestamps[cacheKey] = now
                Log.i("OSCAR", "Respuesta guardada en caché")
            }
        }
    }

    fun getPropiedades(){
        viewModelScope.launch {
            val response = analisisUseCase.invoke()
            _propiedades.value = response
            Log.i("OSCAR", response.toString())
        }
    }
}