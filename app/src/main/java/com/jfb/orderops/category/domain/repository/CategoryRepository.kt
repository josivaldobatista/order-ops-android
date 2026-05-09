package com.jfb.orderops.category.domain.repository

import com.jfb.orderops.category.domain.model.Category
import com.jfb.orderops.core.result.AppResult

interface CategoryRepository {

    suspend fun list(): AppResult<List<Category>>

    suspend fun create(
        name: String
    ): AppResult<Category>
}