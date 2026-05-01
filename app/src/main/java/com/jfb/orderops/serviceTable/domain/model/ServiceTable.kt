package com.jfb.orderops.serviceTable.domain.model

data class ServiceTable(
    val id: Long,
    val number: String,
    val capacity: Int,
    val status: ServiceTableStatus
)

enum class ServiceTableStatus {
    AVAILABLE,
    OCCUPIED,
    RESERVED,
    INACTIVE,
    UNKNOWN
}