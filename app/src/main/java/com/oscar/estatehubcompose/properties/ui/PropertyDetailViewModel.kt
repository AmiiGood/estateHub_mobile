package com.oscar.estatehubcompose.properties.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oscar.estatehubcompose.citas.data.network.request.CitaData
import com.oscar.estatehubcompose.citas.data.network.request.CitaRequest
import com.oscar.estatehubcompose.citas.domain.CitaUseCase
import com.oscar.estatehubcompose.helpers.DataStoreManager
import com.oscar.estatehubcompose.properties.data.network.response.PropiedadDetail
import com.oscar.estatehubcompose.properties.domain.PropertyDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PropertyDetailViewModel @Inject constructor(
    private val propertyDetailUseCase: PropertyDetailUseCase,
    private val citaUseCase: CitaUseCase,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val _propertyDetail = MutableLiveData<PropiedadDetail?>()
    val propertyDetail: LiveData<PropiedadDetail?> = _propertyDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _citaSuccess = MutableLiveData<Boolean>()
    val citaSuccess: LiveData<Boolean> = _citaSuccess

    private val _citaError = MutableLiveData<String?>()
    val citaError: LiveData<String?> = _citaError

    private val _isCreatingCita = MutableLiveData<Boolean>()
    val isCreatingCita: LiveData<Boolean> = _isCreatingCita

    private val _currentUserId = MutableLiveData<Int?>()
    val currentUserId: LiveData<Int?> = _currentUserId

    init {
        loadCurrentUserId()
    }

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

    fun createCita(idPropiedad: Int, idUsuario: Int, fecha: String) {
        viewModelScope.launch {
            _isCreatingCita.value = true
            _citaError.value = null
            _citaSuccess.value = false

            try {
                val token = dataStoreManager.getToken().first() ?: ""
                if (token.isEmpty()) {
                    _citaError.value = "No hay sesión activa"
                    _isCreatingCita.value = false
                    return@launch
                }

                val citaRequest = CitaRequest(
                    cita = CitaData(
                        idPropiedad = idPropiedad,
                        idUsuario = idUsuario,
                        fecha = fecha,
                        estatus = "en_proceso"
                    )
                )

                val result = citaUseCase.invoke(token, citaRequest)

                result.onSuccess { response ->
                    _citaSuccess.value = true
                    Log.i("PropertyDetailVM", "Cita creada: ${response.message}")
                }.onFailure { exception ->
                    _citaError.value = exception.message ?: "Error al crear la cita"
                    Log.e("PropertyDetailVM", "Error al crear cita: ${exception.message}")
                }
            } catch (e: Exception) {
                _citaError.value = "Error: ${e.message}"
                Log.e("PropertyDetailVM", "Exception al crear cita: ${e.message}")
            } finally {
                _isCreatingCita.value = false
            }
        }
    }

    fun clearCitaMessages() {
        _citaSuccess.value = false
        _citaError.value = null
    }

    private fun loadCurrentUserId() {
        viewModelScope.launch {
            dataStoreManager.getIdUsuario().collect { userId ->
                _currentUserId.value = userId
                Log.i("PropertyDetailVM", "Current user ID: $userId")
            }
        }
    }
}