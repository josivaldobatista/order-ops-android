package com.jfb.orderops.serviceTable.data.repository

import com.jfb.orderops.core.result.AppResult
import com.jfb.orderops.serviceTable.data.dto.CreateServiceTableRequest
import com.jfb.orderops.serviceTable.data.dto.toDomain
import com.jfb.orderops.serviceTable.data.remote.ServiceTableApi
import com.jfb.orderops.serviceTable.domain.model.ServiceTable
import com.jfb.orderops.serviceTable.domain.repository.ServiceTableRepository

class ServiceTableRepositoryImpl(
    private val api: ServiceTableApi
) : ServiceTableRepository {

    override suspend fun list(): AppResult<List<ServiceTable>> {
        return try {
            val serviceTables = api.list()
                .map { it.toDomain() }

            AppResult.Success(serviceTables)
        } catch (e: Exception) {
            AppResult.Error(
                message = e.message ?: "Erro ao carregar mesas.",
                throwable = e
            )
        }
    }

    override suspend fun create(
        number: String,
        capacity: Int
    ): AppResult<ServiceTable> {
        return try {
            val table = api.create(
                CreateServiceTableRequest(
                    number = number,
                    capacity = capacity
                )
            ).toDomain()

            AppResult.Success(table)
        } catch (e: Exception) {
            AppResult.Error(
                message = e.message ?: "Erro ao criar mesa.",
                throwable = e
            )
        }
    }
}