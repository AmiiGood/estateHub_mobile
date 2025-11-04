package com.oscar.estatehubcompose.register.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oscar.estatehubcompose.register.data.network.request.RegisterRequest
import com.oscar.estatehubcompose.register.data.network.request.Usuario
import com.oscar.estatehubcompose.register.domain.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private var _email = MutableLiveData<String>()
    private var _password = MutableLiveData<String>()
    private var _nombre = MutableLiveData<String>()
    private var _apellidoPaterno = MutableLiveData<String>()
    private var _apellidoMaterno = MutableLiveData<String>()
    private var _telefono = MutableLiveData<String>()
    private var _isLoading = MutableLiveData<Boolean>()
    private var _registerSuccess = MutableLiveData<Boolean>()
    private var _errorMessage = MutableLiveData<String>()

    // Estados de validación para cada campo
    private var _emailError = MutableLiveData<String>()
    private var _passwordError = MutableLiveData<String>()
    private var _nombreError = MutableLiveData<String>()
    private var _apellidoPaternoError = MutableLiveData<String>()
    private var _apellidoMaternoError = MutableLiveData<String>()
    private var _telefonoError = MutableLiveData<String>()

    val email: LiveData<String> = _email
    val password: LiveData<String> = _password
    val nombre: LiveData<String> = _nombre
    val apellidoPaterno: LiveData<String> = _apellidoPaterno
    val apellidoMaterno: LiveData<String> = _apellidoMaterno
    val telefono: LiveData<String> = _telefono
    val isLoading: LiveData<Boolean> = _isLoading
    val registerSuccess: LiveData<Boolean> = _registerSuccess
    val errorMessage: LiveData<String> = _errorMessage

    val emailError: LiveData<String> = _emailError
    val passwordError: LiveData<String> = _passwordError
    val nombreError: LiveData<String> = _nombreError
    val apellidoPaternoError: LiveData<String> = _apellidoPaternoError
    val apellidoMaternoError: LiveData<String> = _apellidoMaternoError
    val telefonoError: LiveData<String> = _telefonoError

    fun setEmail(email: String) {
        _email.value = email
        validateEmail(email)
    }

    fun setPassword(password: String) {
        _password.value = password
        validatePassword(password)
    }

    fun setNombre(nombre: String) {
        _nombre.value = nombre
        validateNombre(nombre)
    }

    fun setApellidoPaterno(apellidoPaterno: String) {
        _apellidoPaterno.value = apellidoPaterno
        validateApellidoPaterno(apellidoPaterno)
    }

    fun setApellidoMaterno(apellidoMaterno: String) {
        _apellidoMaterno.value = apellidoMaterno
        validateApellidoMaterno(apellidoMaterno)
    }

    fun setTelefono(telefono: String) {
        if (telefono.all { it.isDigit() } || telefono.isEmpty()) {
            _telefono.value = telefono
            validateTelefono(telefono)
        }
    }

    // Validaciones individuales
    private fun validateEmail(email: String) {
        _emailError.value = when {
            email.isEmpty() -> ""
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                "Correo electrónico inválido"
            else -> ""
        }
    }

    private fun validatePassword(password: String) {
        _passwordError.value = when {
            password.isEmpty() -> ""
            password.length < 6 -> "La contraseña debe tener al menos 6 caracteres"
            else -> ""
        }
    }

    private fun validateNombre(nombre: String) {
        _nombreError.value = when {
            nombre.isEmpty() -> ""
            nombre.length < 2 -> "El nombre debe tener al menos 2 caracteres"
            !nombre.all { it.isLetter() || it.isWhitespace() } ->
                "El nombre solo puede contener letras"
            else -> ""
        }
    }

    private fun validateApellidoPaterno(apellido: String) {
        _apellidoPaternoError.value = when {
            apellido.isEmpty() -> ""
            apellido.length < 2 -> "El apellido debe tener al menos 2 caracteres"
            !apellido.all { it.isLetter() || it.isWhitespace() } ->
                "El apellido solo puede contener letras"
            else -> ""
        }
    }

    private fun validateApellidoMaterno(apellido: String) {
        _apellidoMaternoError.value = when {
            apellido.isEmpty() -> ""
            apellido.length < 2 -> "El apellido debe tener al menos 2 caracteres"
            !apellido.all { it.isLetter() || it.isWhitespace() } ->
                "El apellido solo puede contener letras"
            else -> ""
        }
    }

    private fun validateTelefono(telefono: String) {
        _telefonoError.value = when {
            telefono.isEmpty() -> ""
            telefono.length != 10 -> "El teléfono debe tener 10 dígitos"
            else -> ""
        }
    }

    private fun validateAllFields(
        email: String,
        password: String,
        nombre: String,
        apellidoPaterno: String,
        apellidoMaterno: String,
        telefono: String
    ): Boolean {
        validateEmail(email)
        validatePassword(password)
        validateNombre(nombre)
        validateApellidoPaterno(apellidoPaterno)
        validateApellidoMaterno(apellidoMaterno)
        validateTelefono(telefono)

        return email.isNotEmpty() &&
                password.isNotEmpty() &&
                nombre.isNotEmpty() &&
                apellidoPaterno.isNotEmpty() &&
                apellidoMaterno.isNotEmpty() &&
                telefono.isNotEmpty() &&
                _emailError.value.isNullOrEmpty() &&
                _passwordError.value.isNullOrEmpty() &&
                _nombreError.value.isNullOrEmpty() &&
                _apellidoPaternoError.value.isNullOrEmpty() &&
                _apellidoMaternoError.value.isNullOrEmpty() &&
                _telefonoError.value.isNullOrEmpty()
    }

    fun onRegister(
        email: String,
        password: String,
        nombre: String,
        apellidoPaterno: String,
        apellidoMaterno: String,
        telefono: String
    ) {
        // Validar todos los campos
        if (!validateAllFields(email, password, nombre, apellidoPaterno, apellidoMaterno, telefono)) {
            _errorMessage.value = "Por favor completa todos los campos correctamente"
            return
        }

        _isLoading.value = true
        _errorMessage.value = ""
        _registerSuccess.value = false

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val fechaRegistro = dateFormat.format(Date())

        val usuario = Usuario(
            email = email,
            password = password,
            nombre = nombre,
            apellidoPaterno = apellidoPaterno,
            apellidoMaterno = apellidoMaterno,
            telefono = telefono,
            fechaRegistro = fechaRegistro
        )

        val registerRequest = RegisterRequest(usuario = usuario)

        viewModelScope.launch {
            val result = registerUseCase.invoke(registerRequest)

            result.onSuccess { response ->
                _isLoading.value = false
                _registerSuccess.value = true
                _errorMessage.value = ""
            }.onFailure { exception ->
                _isLoading.value = false
                _registerSuccess.value = false

                _errorMessage.value = when {
                    exception.message?.contains("400") == true ->
                        "Campos requeridos faltantes o vacíos"
                    exception.message?.contains("409") == true ->
                        "El correo ya está registrado"
                    exception.message?.contains("500") == true ->
                        "Error en el servidor. Intenta más tarde"
                    exception is java.net.UnknownHostException ->
                        "No hay conexión a internet"
                    exception is java.net.SocketTimeoutException ->
                        "Tiempo de espera agotado. Intenta de nuevo"
                    else ->
                        exception.message ?: "Error inesperado"
                }
            }
        }
    }

    fun clearMessages() {
        _errorMessage.value = ""
        _registerSuccess.value = false
    }
}