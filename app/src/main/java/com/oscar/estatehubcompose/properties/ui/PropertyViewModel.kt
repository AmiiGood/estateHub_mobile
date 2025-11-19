package com.oscar.estatehubcompose.properties.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oscar.estatehubcompose.helpers.DataStoreManager
import com.oscar.estatehubcompose.properties.data.network.response.Propiedad
import com.oscar.estatehubcompose.properties.domain.PropertyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.Normalizer
import javax.inject.Inject

@HiltViewModel
class PropertyViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val propertyUseCase: PropertyUseCase
): ViewModel() {

    private val _propiedades = MutableLiveData<List<Propiedad>>()
    val propiedades: LiveData<List<Propiedad>> = _propiedades

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _selectedCategory = MutableLiveData("Todo")
    val selectedCategory: LiveData<String> = _selectedCategory

    private val _currentFilters = MutableLiveData(PropertyFilters())
    val currentFilters: LiveData<PropertyFilters> = _currentFilters

    private var currentSearchQuery: String = ""

    init {
        Log.i("OSCAR", "PropertyViewModel inicializado")
        loadPropiedades()
    }

    private fun String.normalizeText(): String {
        val normalized = Normalizer.normalize(this, Normalizer.Form.NFD)
        return normalized.replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "").lowercase()
    }

    fun loadPropiedades(
        ciudad: String? = null,
        estado: String? = null,
        tipoPropiedad: String? = null,
        precioMin: Double? = null,
        precioMax: Double? = null,
        habitaciones: Int? = null,
        banios: Int? = null
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val response = propertyUseCase.invoke(
                    ciudad = ciudad,
                    estado = estado,
                    tipoPropiedad = tipoPropiedad,
                    precioMin = precioMin,
                    precioMax = precioMax,
                    habitaciones = habitaciones,
                    banios = banios
                )
                if (response != null && response.success) {
                    _propiedades.value = response.data
                    Log.i("OSCAR", "Propiedades cargadas: ${response.data.size}")
                } else {
                    _error.value = "Error al cargar las propiedades"
                    Log.e("OSCAR", "Error en la respuesta")
                }
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
                Log.e("OSCAR", "Exception: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateCategory(category: String) {
        _selectedCategory.value = category
        applyFiltersWithCategory(category)
    }

    fun applyFilters(filters: PropertyFilters) {
        _currentFilters.value = filters
        applyFiltersWithCategory(_selectedCategory.value ?: "Todo")
    }

    fun clearFilters() {
        _currentFilters.value = PropertyFilters()
        _selectedCategory.value = "Todo"
        currentSearchQuery = ""
        loadPropiedades()
    }

    private fun applyFiltersWithCategory(category: String) {
        val filters = _currentFilters.value ?: PropertyFilters()

        val tipoPropiedad = when(category) {
            "Casas" -> "residencial"
            "Cuartos" -> "departamento"
            else -> filters.tipoPropiedad
        }

        val ciudad = if (currentSearchQuery.isNotEmpty()) currentSearchQuery else filters.ciudad

        loadPropiedades(
            ciudad = ciudad,
            estado = filters.estado,
            tipoPropiedad = if (category == "En venta" || category == "En renta" || category == "Todo")
                filters.tipoPropiedad else tipoPropiedad,
            precioMin = filters.precioMin,
            precioMax = filters.precioMax,
            habitaciones = filters.habitaciones,
            banios = filters.banios
        )
    }

    fun searchByLocation(query: String) {
        currentSearchQuery = query

        if (query.isEmpty()) {
            applyFiltersWithCategory(_selectedCategory.value ?: "Todo")
            return
        }

        val normalizedQuery = query.normalizeText()
        val filters = _currentFilters.value ?: PropertyFilters()
        val category = _selectedCategory.value ?: "Todo"

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val response = propertyUseCase.invoke(
                    ciudad = null,
                    estado = filters.estado,
                    tipoPropiedad = when(category) {
                        "Casas" -> "residencial"
                        "Cuartos" -> "departamento"
                        else -> filters.tipoPropiedad
                    },
                    precioMin = filters.precioMin,
                    precioMax = filters.precioMax,
                    habitaciones = filters.habitaciones,
                    banios = filters.banios
                )

                if (response != null && response.success) {
                    val filteredData = response.data.filter { propiedad ->
                        val normalizedCiudad = propiedad.ciudad.normalizeText()
                        val normalizedEstado = propiedad.estado.normalizeText()
                        val normalizedDireccion = propiedad.direccion.normalizeText()

                        normalizedCiudad.contains(normalizedQuery) ||
                                normalizedEstado.contains(normalizedQuery) ||
                                normalizedDireccion.contains(normalizedQuery)
                    }

                    _propiedades.value = filteredData
                    Log.i("OSCAR", "Propiedades filtradas: ${filteredData.size}")
                } else {
                    _error.value = "Error al cargar las propiedades"
                    Log.e("OSCAR", "Error en la respuesta")
                }
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
                Log.e("OSCAR", "Exception: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun refreshPropiedades() {
        val filters = _currentFilters.value ?: PropertyFilters()
        val category = _selectedCategory.value ?: "Todo"

        applyFiltersWithCategory(category)
    }
}

data class PropertyFilters(
    val ciudad: String? = null,
    val estado: String? = null,
    val tipoPropiedad: String? = null,
    val precioMin: Double? = null,
    val precioMax: Double? = null,
    val habitaciones: Int? = null,
    val banios: Int? = null
)