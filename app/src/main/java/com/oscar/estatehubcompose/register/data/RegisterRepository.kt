package com.oscar.estatehubcompose.register.data

import com.oscar.estatehubcompose.register.data.network.RegisterService
import com.oscar.estatehubcompose.register.data.network.request.RegisterRequest
import com.oscar.estatehubcompose.register.data.network.response.RegisterResponse
import javax.inject.Inject

class RegisterRepository @Inject constructor(
    private val registerService: RegisterService
) {
    suspend fun register(registerRequest: RegisterRequest): Pair<RegisterResponse?, Int?> {
        return registerService.register(registerRequest)
    }
}