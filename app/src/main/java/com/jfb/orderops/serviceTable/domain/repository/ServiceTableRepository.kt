package com.jfb.orderops.serviceTable.domain.repository

import com.jfb.orderops.core.result.AppResult
import com.jfb.orderops.serviceTable.domain.model.ServiceTable

interface ServiceTableRepository {

    suspend fun list(): AppResult<List<ServiceTable>>
}