package com.jfb.orderops.product.domain.repository

import com.jfb.orderops.core.result.AppResult
import com.jfb.orderops.product.domain.model.Product

interface ProductRepository {
    suspend fun list(): AppResult<List<Product>>
}