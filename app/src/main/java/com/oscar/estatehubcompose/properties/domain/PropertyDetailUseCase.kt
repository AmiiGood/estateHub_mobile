package com.oscar.estatehubcompose.properties.domain

import com.oscar.estatehubcompose.properties.data.PropertyRepository
import com.oscar.estatehubcompose.properties.data.network.response.PropertyDetailResponse
import javax.inject.Inject

class PropertyDetailUseCase @Inject constructor(
    private val propertyRepository: PropertyRepository
) {
    suspend operator fun invoke(id: Int): PropertyDetailResponse? {
        return propertyRepository.getPropertyDetail(id)
    }
}