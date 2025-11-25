package com.oscar.estatehubcompose.perfil.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oscar.estatehubcompose.perfil.data.network.response.PropiedadesResponse
import com.oscar.estatehubcompose.perfil.data.network.response.UsuarioResponse
import com.oscar.estatehubcompose.perfil.domain.PerfilUseCase
import com.oscar.estatehubcompose.perfil.domain.PropiedadesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PerfilViewModel @Inject constructor(private var perfilUseCase: PerfilUseCase,
                                          private var propiedadesUseCase: PropiedadesUseCase): ViewModel() {


    private var _perfilData = MutableLiveData<UsuarioResponse?>();
    var perfilData: LiveData<UsuarioResponse?> = _perfilData;

    private var _propiedadesData = MutableLiveData<PropiedadesResponse?>();
    var propiedadesData: LiveData<PropiedadesResponse?> = _propiedadesData;


    fun getUsuario(){
        viewModelScope.launch {

            val response = perfilUseCase.invoke();
            _perfilData.value = response;
        }
    }

    fun getPropiedades(){
        viewModelScope.launch {
            val response = propiedadesUseCase.invoke();
            _propiedadesData.value = response;

            Log.i("OSCAR", response.toString());
        }
    }


}