package com.jfb.orderops.company.domain.model

data class Company(
    val id: Long,
    val name: String,
    val document: String,
    val phone: String?,
    val address: CompanyAddress?
)

data class CompanyAddress(
    val street: String,
    val number: String,
    val complement: String?,
    val city: String,
    val state: String,
    val zipCode: String
)