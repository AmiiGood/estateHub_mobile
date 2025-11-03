package com.oscar.estatehubcompose.login.data.network

import android.util.Log
import com.oscar.estatehubcompose.login.data.network.request.LoginRequest
import com.oscar.estatehubcompose.login.data.network.response.LoginResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.IOException
import javax.inject.Inject

class LoginService @Inject constructor(private val loginClient: LoginClient){


    suspend fun login(loginRequest: LoginRequest): Result<LoginResponse?>{

        return withContext(Dispatchers.IO){

            try{
                val response = loginClient.login(loginRequest);

                if(response.isSuccessful && response.body() != null){
                    Result.success(response.body());
                }else{
                    val errorMsg = when (response.code()) {
                        401 -> "Credenciales inválidas"
                        404 -> "Usuario no encontrado"
                        500 -> "Error del servidor"
                        else -> "Error ${response.code()}: ${response.message()}"
                    }
                    Result.failure(Exception(errorMsg));
                }

            }catch (e: IOException){
                Log.e("OSCAR", "Error de red: ${e.message}")
                Result.failure(Exception("Sin conexión a internet"));
            }
        };

    };

}