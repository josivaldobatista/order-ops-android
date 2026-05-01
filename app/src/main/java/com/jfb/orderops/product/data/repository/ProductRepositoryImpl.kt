package com.jfb.orderops.product.data.repository

import com.jfb.orderops.core.result.AppResult
import com.jfb.orderops.product.data.dto.toDomain
import com.jfb.orderops.product.data.remote.ProductApi
import com.jfb.orderops.product.domain.model.Product
import com.jfb.orderops.product.domain.repository.ProductRepository

class ProductRepositoryImpl(
    private val api: ProductApi
) : ProductRepository {

    override suspend fun list(): AppResult<List<Product>> {
        return try {
            val products = api.list().map { it.toDomain() }
            AppResult.Success(products)
        } catch (e: Exception) {
            AppResult.Error(
                message = e.message ?: "Erro ao carregar produtos.",
                throwable = e
            )
        }
    }
}