package com.oscar.estatehubcompose.login.data

import android.util.Log
import com.oscar.estatehubcompose.helpers.DataStoreManager
import com.oscar.estatehubcompose.login.data.network.LoginService
import com.oscar.estatehubcompose.login.data.network.request.LoginRequest
import com.oscar.estatehubcompose.login.data.network.response.LoginResponse
import javax.inject.Inject

class LoginRepository @Inject constructor(private val loginService: LoginService){

    suspend fun login(loginRequest: LoginRequest): Result<LoginResponse?>{

        val response =  loginService.login(loginRequest);


        Log.i("Oscar", response.toString());
        return response;

    };
};