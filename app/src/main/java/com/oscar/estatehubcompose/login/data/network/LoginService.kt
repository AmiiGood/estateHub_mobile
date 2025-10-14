package com.oscar.estatehubcompose.login.data.network

import com.oscar.estatehubcompose.login.data.network.request.LoginRequest
import com.oscar.estatehubcompose.login.data.network.response.LoginResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoginService @Inject constructor(private val loginClient: LoginClient){


    suspend fun login(loginRequest: LoginRequest): LoginResponse?{

        return withContext(Dispatchers.IO){

            val response = loginClient.login(loginRequest);
            response.body();
        };

    };

}