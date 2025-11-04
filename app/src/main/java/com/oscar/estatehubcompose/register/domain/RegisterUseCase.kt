package com.oscar.estatehubcompose.register.domain

import com.oscar.estatehubcompose.register.data.RegisterRepository
import com.oscar.estatehubcompose.register.data.network.request.RegisterRequest
import com.oscar.estatehubcompose.register.data.network.response.RegisterResponse
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val registerRepository: RegisterRepository
) {
    suspend operator fun invoke(registerRequest: RegisterRequest): Result<RegisterResponse> {
        return registerRepository.register(registerRequest)
    }
}