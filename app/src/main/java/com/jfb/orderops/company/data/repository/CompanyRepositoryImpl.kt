package com.jfb.orderops.company.data.repository

import com.jfb.orderops.company.data.dto.toDomain
import com.jfb.orderops.company.data.remote.CompanyApi
import com.jfb.orderops.company.domain.model.Company
import com.jfb.orderops.company.domain.repository.CompanyRepository
import com.jfb.orderops.core.result.AppResult

class CompanyRepositoryImpl(
    private val api: CompanyApi
) : CompanyRepository {

    override suspend fun getById(id: Long): AppResult<Company> {
        return try {
            val company = api.getById(id).toDomain()
            AppResult.Success(company)
        } catch (e: Exception) {
            AppResult.Error(
                message = e.message ?: "Erro ao carregar empresa.",
                throwable = e
            )
        }
    }
}