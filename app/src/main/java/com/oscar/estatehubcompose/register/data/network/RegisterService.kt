package com.oscar.estatehubcompose.register.data.network

import com.oscar.estatehubcompose.register.data.network.request.RegisterRequest
import com.oscar.estatehubcompose.register.data.network.response.RegisterResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RegisterService @Inject constructor(private val registerClient: RegisterClient) {

    suspend fun register(registerRequest: RegisterRequest): RegisterResponse? {
        return withContext(Dispatchers.IO) {
            val response = registerClient.register(registerRequest)
            response.body()
        }
    }
}