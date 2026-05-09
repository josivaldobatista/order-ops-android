package com.jfb.orderops.product.domain.repository

import com.jfb.orderops.core.result.AppResult
import com.jfb.orderops.product.domain.model.Product
import java.math.BigDecimal

interface ProductRepository {

    suspend fun list(): AppResult<List<Product>>

    suspend fun create(
        name: String,
        description: String?,
        price: BigDecimal,
        categoryId: Long?,
        active: Boolean
    ): AppResult<Product>
}