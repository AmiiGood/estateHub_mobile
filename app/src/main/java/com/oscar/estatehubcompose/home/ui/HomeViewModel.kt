package com.oscar.estatehubcompose.home.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oscar.estatehubcompose.helpers.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val dataStoreManager: DataStoreManager): ViewModel() {

    init {

        Log.i("OSCAR", "Ta funcionando el vm")

        viewModelScope.launch {
            dataStoreManager.getToken().collect { token ->
                if (token != null) {
                    Log.i("OSCAR", "Token encontrado: $token")
                } else {
                    Log.i("OSCAR", "No hay token guardado")
                }
            }
        }
    }


}