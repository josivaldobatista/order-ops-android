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

    override suspend fun reserve(id: Long): AppResult<ServiceTable> {
        return try {
            val table = api.reserve(id).toDomain()
            AppResult.Success(table)
        } catch (e: Exception) {
            AppResult.Error(
                message = e.message ?: "Erro ao reservar mesa.",
                throwable = e
            )
        }
    }

    override suspend fun occupy(id: Long): AppResult<ServiceTable> {
        return try {
            val table = api.occupy(id).toDomain()
            AppResult.Success(table)
        } catch (e: Exception) {
            AppResult.Error(
                message = e.message ?: "Erro ao ocupar mesa.",
                throwable = e
            )
        }
    }

    override suspend fun release(id: Long): AppResult<ServiceTable> {
        return try {
            val table = api.release(id).toDomain()
            AppResult.Success(table)
        } catch (e: Exception) {
            AppResult.Error(
                message = e.message ?: "Erro ao liberar mesa.",
                throwable = e
            )
        }
    }

    override suspend fun deactivate(id: Long): AppResult<Unit> {
        return try {
            api.deactivate(id)
            AppResult.Success(Unit)
        } catch (e: Exception) {
            AppResult.Error(
                message = e.message ?: "Erro ao desativar mesa.",
                throwable = e
            )
        }
    }
}