package com.jfb.orderops.company.domain.repository

import com.jfb.orderops.company.domain.model.Company
import com.jfb.orderops.core.result.AppResult

interface CompanyRepository {

    suspend fun getById(id: Long): AppResult<Company>
}