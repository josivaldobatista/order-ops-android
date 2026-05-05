package com.jfb.orderops.company.domain.usecase

import com.jfb.orderops.company.domain.model.Company
import com.jfb.orderops.company.domain.repository.CompanyRepository
import com.jfb.orderops.core.result.AppResult

class GetCompanyByIdUseCase(
    private val repository: CompanyRepository
) {

    suspend fun execute(id: Long): AppResult<Company> {
        if (id <= 0) {
            return AppResult.Error("Empresa inválida.")
        }

        return repository.getById(id)
    }
}