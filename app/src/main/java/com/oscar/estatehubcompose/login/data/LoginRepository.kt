package com.oscar.estatehubcompose.login.data

import com.oscar.estatehubcompose.login.data.network.LoginService
import com.oscar.estatehubcompose.login.data.network.request.LoginRequest
import com.oscar.estatehubcompose.login.data.network.response.LoginResponse
import javax.inject.Inject

class LoginRepository @Inject constructor(private val loginService: LoginService){

    suspend fun login(loginRequest: LoginRequest): LoginResponse?{
        val response = loginService.login(loginRequest);

      return response;
    };
};