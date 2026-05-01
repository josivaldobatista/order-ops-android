package com.jfb.orderops.serviceTable.data.dto

data class ServiceTableResponse(
    val id: Long,
    val companyId: Long,
    val number: String,
    val capacity: Int,
    val status: String,
    val createdAt: String,
    val updatedAt: String
)