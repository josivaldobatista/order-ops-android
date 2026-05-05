package com.jfb.orderops.company.data.dto

data class CompanyResponse(
    val id: Long,
    val name: String,
    val document: String,
    val phone: String?,
    val address: CompanyAddressResponse?,
    val plan: String,
    val billingType: String,
    val status: String,
    val trialEndsAt: String?,
    val createdAt: String?,
    val updatedAt: String?
)

data class CompanyAddressResponse(
    val street: String,
    val number: String,
    val complement: String?,
    val city: String,
    val state: String,
    val zipCode: String
)