package com.jfb.orderops.category.domain.usecase

import com.jfb.orderops.category.domain.model.Category
import com.jfb.orderops.category.domain.repository.CategoryRepository
import com.jfb.orderops.core.result.AppResult

class CreateCategoryUseCase(
    private val repository: CategoryRepository
) {

    suspend fun execute(
        name: String
    ): AppResult<Category> {

        if (name.isBlank()) {
            return AppResult.Error(
                "Nome da categoria é obrigatório."
            )
        }

        return repository.create(
            name = name.trim()
        )
    }
}