package com.jfb.orderops.serviceTable.data.dto

import com.jfb.orderops.serviceTable.domain.model.ServiceTable
import com.jfb.orderops.serviceTable.domain.model.ServiceTableStatus

fun ServiceTableResponse.toDomain(): ServiceTable {
    return ServiceTable(
        id = id,
        number = number,
        capacity = capacity,
        status = runCatching {
            ServiceTableStatus.valueOf(status)
        }.getOrDefault(ServiceTableStatus.UNKNOWN)
    )
}