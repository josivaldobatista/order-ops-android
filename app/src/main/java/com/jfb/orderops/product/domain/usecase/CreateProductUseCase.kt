package com.jfb.orderops.product.domain.usecase

import com.jfb.orderops.core.result.AppResult
import com.jfb.orderops.product.domain.model.Product
import com.jfb.orderops.product.domain.repository.ProductRepository
import java.math.BigDecimal

class CreateProductUseCase(
    private val repository: ProductRepository
) {

    suspend fun execute(
        name: String,
        description: String?,
        price: BigDecimal,
        categoryId: Long?,
        active: Boolean = true
    ): AppResult<Product> {
        if (name.isBlank()) {
            return AppResult.Error("Nome do produto é obrigatório.")
        }

        if (price <= BigDecimal.ZERO) {
            return AppResult.Error("Preço deve ser maior que zero.")
        }

        if (categoryId == null) {
            return AppResult.Error("Categoria do produto é obrigatória.")
        }

        return repository.create(
            name = name.trim(),
            description = description?.trim()?.takeIf { it.isNotBlank() },
            price = price,
            categoryId = categoryId,
            active = active
        )
    }
}