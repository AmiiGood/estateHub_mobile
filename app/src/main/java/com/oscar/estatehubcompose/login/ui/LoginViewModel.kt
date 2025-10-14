package com.oscar.estatehubcompose.login.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oscar.estatehubcompose.login.data.network.request.LoginRequest
import com.oscar.estatehubcompose.login.domain.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginUseCase: LoginUseCase): ViewModel() {

    fun onLogin(user: String, password: String){
        val loginRequest: LoginRequest = LoginRequest(user, password);

        viewModelScope.launch {
            val response = loginUseCase.invoke(loginRequest);
        }
    };
}