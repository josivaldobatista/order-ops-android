package com.jfb.orderops.category.data.dto

data class CategoryResponse(
    val id: Long,
    val companyId: Long,
    val name: String,
    val active: Boolean,
    val createdAt: String?,
    val updatedAt: String?
)