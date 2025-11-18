package com.oscar.estatehubcompose.analisis.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oscar.estatehubcompose.analisis.Models.GeocodificadorInfo
import com.oscar.estatehubcompose.analisis.data.network.request.GeocodificadorRequest
import com.oscar.estatehubcompose.analisis.data.network.response.ParsedGeminiResponse
import com.oscar.estatehubcompose.analisis.domain.AnalisisUseCase
import com.oscar.estatehubcompose.analisis.domain.GeminiUseCase
import com.oscar.estatehubcompose.analisis.domain.geocodificadorUseCase
import com.oscar.estatehubcompose.helpers.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnalisisViewModel @Inject constructor(private val analisisUseCase: AnalisisUseCase,
                                            private val geocodificadorUseCase: geocodificadorUseCase,
                                            private val geminiUseCase: GeminiUseCase,
                                            private val dataStoreManager: DataStoreManager): ViewModel(){


    private var _data = MutableLiveData< GeocodificadorInfo?>();
    var data: LiveData<GeocodificadorInfo?> = _data;

    private var _dataGemini = MutableLiveData<ParsedGeminiResponse?>();
    var dataGemini: LiveData<ParsedGeminiResponse?> = _dataGemini;
    private var _latitud = MutableLiveData<Double>();
    private var _longitud = MutableLiveData<Double>();
    private var _codigoPostal = MutableLiveData<Int>();
    fun setLatitud(latitud: Double){
        _latitud.value = latitud;
    }
    fun setLongitud(longitud: Double){
        _longitud.value = longitud;
    }
    fun setCodigoPostal(codigoPostal: Int){
        _codigoPostal.value = codigoPostal;
    }




    fun getData(latitud: Double, longitud: Double){

        var geocodificadorRequest = GeocodificadorRequest(latitud, longitud, listOf(1,2,3));

        viewModelScope.launch {
            val response = geocodificadorUseCase.invoke(geocodificadorRequest);
            _data.value = response;

            Log.i("OSCAR", data.value.toString());
        }
    }

    fun analizarGemini(colonia: String, codigoPostal: String, ciudad: String, estado: String, geocodificadorInfo: GeocodificadorInfo?){
        viewModelScope.launch {
            val response = geminiUseCase.invoke(colonia, codigoPostal, ciudad, estado, geocodificadorInfo);
            _dataGemini.value = response;
            Log.i("OSCAR", response.toString());
        }
    }







}