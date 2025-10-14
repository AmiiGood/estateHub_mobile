package com.oscar.estatehubcompose.login.domain

import com.oscar.estatehubcompose.login.data.LoginRepository
import com.oscar.estatehubcompose.login.data.network.request.LoginRequest
import com.oscar.estatehubcompose.login.data.network.response.LoginResponse
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val loginRepository: LoginRepository) {

    suspend operator fun invoke(loginRequest: LoginRequest): LoginResponse?{
        var loginResponse = loginRepository.login(loginRequest);
        return loginResponse;
    };
}