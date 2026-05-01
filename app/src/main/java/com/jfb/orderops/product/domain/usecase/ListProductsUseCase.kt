package com.jfb.orderops.product.domain.usecase

import com.jfb.orderops.core.result.AppResult
import com.jfb.orderops.product.domain.model.Product
import com.jfb.orderops.product.domain.repository.ProductRepository

class ListProductsUseCase(
    private val repository: ProductRepository
) {
    suspend fun execute(): AppResult<List<Product>> {
        return repository.list()
    }
}