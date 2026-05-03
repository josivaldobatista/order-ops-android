package com.jfb.orderops.product.domain.usecase

import com.jfb.orderops.core.result.AppResult
import com.jfb.orderops.product.domain.model.Product
import com.jfb.orderops.product.domain.repository.ProductRepository

class CreateProductUseCase(
    private val repository: ProductRepository
) {

    suspend fun execute(
        name: String,
        description: String?,
        price: Double
    ): AppResult<Product> {
        if (name.isBlank()) {
            return AppResult.Error("Nome do produto é obrigatório.")
        }

        if (price <= 0) {
            return AppResult.Error("Preço deve ser maior que zero.")
        }

        return repository.create(
            name = name.trim(),
            description = description?.trim()?.takeIf { it.isNotBlank() },
            price = price
        )
    }
}