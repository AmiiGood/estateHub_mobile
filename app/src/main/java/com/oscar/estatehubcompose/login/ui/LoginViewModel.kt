package com.oscar.estatehubcompose.login.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oscar.estatehubcompose.login.data.network.request.LoginRequest
import com.oscar.estatehubcompose.login.domain.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginUseCase: LoginUseCase): ViewModel() {

    private var _correo = MutableLiveData<String>();
    private var _contrasenia = MutableLiveData<String>();

    var correo: LiveData<String> = _correo;
    var contrasenia: LiveData<String> = _contrasenia;

    fun setCorreo(correo: String){
        _correo.value = correo;
    }

    fun setContrasenia(contrasenia: String){
        _contrasenia.value = contrasenia;
    }




    fun onLogin(user: String, password: String){
        val loginRequest: LoginRequest = LoginRequest(user, password);

        viewModelScope.launch {
            val response = loginUseCase.invoke(loginRequest);
        }
    };
}