package com.oscar.estatehubcompose.analisis.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.oscar.estatehubcompose.analisis.data.network.AnalisisService
import com.oscar.estatehubcompose.analisis.data.network.request.AnalisisRequest
import com.oscar.estatehubcompose.analisis.data.network.request.GeocodificadorRequest
import com.oscar.estatehubcompose.analisis.data.network.response.AnalisisResponse
import com.oscar.estatehubcompose.analisis.data.network.response.GeocodificadorResponse
import javax.inject.Inject

class AnalisisRepository @Inject constructor(private val analisisService: AnalisisService){


    var _data = MutableLiveData<GeocodificadorResponse?>();

    var data: LiveData<GeocodificadorResponse?> = _data;



    suspend fun analizar(analisisRequest: AnalisisRequest): AnalisisResponse? {

        val response = analisisService.analizar(analisisRequest);

        return response;

    }

    suspend fun geocodificar(geocodificadorRequest: GeocodificadorRequest): GeocodificadorResponse?{
        val response = analisisService.geocodificar(geocodificadorRequest);

        _data.value = response;
        Log.i("OSCAR", _data.value.toString());
        return response;
    }

}