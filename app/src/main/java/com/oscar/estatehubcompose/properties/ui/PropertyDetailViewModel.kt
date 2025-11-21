package com.oscar.estatehubcompose.properties.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oscar.estatehubcompose.properties.data.network.response.PropiedadDetail
import com.oscar.estatehubcompose.properties.domain.PropertyDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PropertyDetailViewModel @Inject constructor(
    private val propertyDetailUseCase: PropertyDetailUseCase
) : ViewModel() {

    private val _propertyDetail = MutableLiveData<PropiedadDetail?>()
    val propertyDetail: LiveData<PropiedadDetail?> = _propertyDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun loadPropertyDetail(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val response = propertyDetailUseCase.invoke(id)
                if (response != null && response.success) {
                    _propertyDetail.value = response.data
                    Log.i("PropertyDetailVM", "Propiedad cargada: ${response.data}")
                } else {
                    _error.value = "Error al cargar la propiedad"
                    Log.e("PropertyDetailVM", "Error en la respuesta")
                }
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
                Log.e("PropertyDetailVM", "Exception: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}