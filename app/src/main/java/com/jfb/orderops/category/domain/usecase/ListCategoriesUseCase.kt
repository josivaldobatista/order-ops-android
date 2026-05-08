package com.jfb.orderops.category.domain.usecase

import com.jfb.orderops.category.domain.model.Category
import com.jfb.orderops.category.domain.repository.CategoryRepository
import com.jfb.orderops.core.result.AppResult

class ListCategoriesUseCase(
    private val repository: CategoryRepository
) {

    suspend fun execute(): AppResult<List<Category>> {
        return repository.list()
    }
}