package com.jfb.orderops.category.data.repository

import com.jfb.orderops.category.data.dto.toDomain
import com.jfb.orderops.category.data.remote.CategoryApi
import com.jfb.orderops.category.domain.model.Category
import com.jfb.orderops.category.domain.repository.CategoryRepository
import com.jfb.orderops.core.result.AppResult

class CategoryRepositoryImpl(
    private val api: CategoryApi
) : CategoryRepository {

    override suspend fun list(): AppResult<List<Category>> {
        return try {
            val categories = api.list()
                .map { it.toDomain() }
                .filter { it.active }

            AppResult.Success(categories)
        } catch (e: Exception) {
            AppResult.Error(
                message = e.message ?: "Erro ao carregar categorias.",
                throwable = e
            )
        }
    }
}