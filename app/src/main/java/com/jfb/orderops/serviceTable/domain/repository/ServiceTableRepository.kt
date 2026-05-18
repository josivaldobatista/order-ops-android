package com.jfb.orderops.serviceTable.domain.repository

import com.jfb.orderops.core.result.AppResult
import com.jfb.orderops.serviceTable.domain.model.ServiceTable

interface ServiceTableRepository {

    suspend fun list(): AppResult<List<ServiceTable>>

    suspend fun create(
        number: String,
        capacity: Int
    ): AppResult<ServiceTable>

    suspend fun reserve(id: Long): AppResult<ServiceTable>

    suspend fun occupy(id: Long): AppResult<ServiceTable>

    suspend fun release(id: Long): AppResult<ServiceTable>

    suspend fun deactivate(id: Long): AppResult<Unit>
}